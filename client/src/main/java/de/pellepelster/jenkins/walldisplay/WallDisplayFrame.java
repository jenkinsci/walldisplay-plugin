package de.pellepelster.jenkins.walldisplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;


import de.pellepelster.jenkins.walldisplay.model.Job;

/**
 * 
 * @author pelle
 */
public class WallDisplayFrame extends javax.swing.JFrame {

    private class JobComperator implements Comparator<Job> {

        /** {@inheritDoc} */
        @Override
        public int compare(Job job1, Job job2) {

            if (job1 == null || job2 == null) {
                return 0;
            }

            return getJobText(job1).compareTo(getJobText(job2));
        }
    }
    private static final long serialVersionUID = 7190596411432294617L;
    private String message = "";
    private JenkinsWorker hudsonWorker;
    private Image bufferedImage;
    private Graphics2D bufferedGraphics;
    private List<Job> jobs = new ArrayList<Job>();
    private String jenkinsUrl;
    private String viewName;
    private final static Color BACKGROUND_COLOR = Color.WHITE;
    private final static Color JOB_TEXT_COLOR = Color.WHITE;
    private final static Color QUEUE_POSITIONS_COLOR = Color.YELLOW;
    private final static int MARGIN = 10;
    private final static int JOB_MARGIN = 20;
    private final static String FONT_NAME = "Arial";
    private final static Color MESSAGE_FONT_COLOR = Color.BLACK;
    private final static int MESSAGE_FONT_SIZE = 24;
    private final static int JOB_ARC_WIDTH = 20;
    private final static int JOB_ARC_HEIGHT = 20;
    private final static String MESSAGE_INITIALIZING = "Initializing Jenkins Wall Display";
    private final static String MESSAGE_ENTER_JEKINS_URL = "Enter Jenkins URL";
    private final static String MESSAGE_NO_URL_ENTERED = "No URL given, exiting";
    private final static String MESSAGE_JEKINS_WALL_DISPLAY = "Jenkins Wall Display";
    private final static int MAX_QUEUE_POSITIONS = 3;

    public WallDisplayFrame() {

        initFrame();
        setTitle(MESSAGE_JEKINS_WALL_DISPLAY);

        String url = JOptionPane.showInputDialog(null, MESSAGE_ENTER_JEKINS_URL, MESSAGE_JEKINS_WALL_DISPLAY, 1);

        if (url != null) {
            this.jenkinsUrl = url;
        } else {
            JOptionPane.showMessageDialog(null, MESSAGE_NO_URL_ENTERED, MESSAGE_JEKINS_WALL_DISPLAY, 1);
            System.exit(1);
        }

        init();
    }

    public WallDisplayFrame(final String jenkinsUrl, String viewName) {
        this.jenkinsUrl = jenkinsUrl;
        this.viewName = viewName;
        initFrame();
        init();
    }

    public WallDisplayFrame(final String jenkinsUrl) {
        this(jenkinsUrl, null);
    }

    private void drawMessage(Graphics2D g2) {

        AttributedString attributedString = new AttributedString(message);
        attributedString.addAttribute(TextAttribute.FONT, getMessageTextFont());
        attributedString.addAttribute(TextAttribute.FOREGROUND, MESSAGE_FONT_COLOR);

        int width = getCanvasWidth();
        int x = getCanvasLeft();
        int y = getCanvasTop();

        AttributedCharacterIterator characterIterator = attributedString.getIterator();
        FontRenderContext fontRenderContext = g2.getFontRenderContext();
        LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator, fontRenderContext);

        while (measurer.getPosition() < characterIterator.getEndIndex()) {

            TextLayout textLayout = measurer.nextLayout(width);
            y += textLayout.getAscent();

            textLayout.draw(g2, x, y);
            y += textLayout.getDescent() + textLayout.getLeading();

        }
    }

    private int getCanvasHeight() {
        return getHeight() - (getInsets().top + getInsets().bottom);
    }

    private int getCanvasLeft() {
        return getInsets().left;
    }

    private int getCanvasTop() {
        return getInsets().top;
    }

    private int getCanvasWidth() {
        return getWidth() - (getInsets().left + getInsets().right);
    }

    private Color getJobColor(Job job) {

        Color result = Color.GRAY;

        if ("red".equals(job.getColor()) || "red_anime".equals(job.getColor())) {
            result = Color.RED;
        } else if ("yellow".equals(job.getColor()) || "yellow_anime".equals(job.getColor())) {
            result = Color.YELLOW;
        } else if ("blue".equals(job.getColor()) || "blue_anime".equals(job.getColor())) {
            result = Color.GREEN;
        } else if ("grey".equals(job.getColor()) || "grey_anime".equals(job.getColor())) {
            result = Color.GRAY;
        } else if ("disabled".equals(job.getColor()) || "disabled_anime".equals(job.getColor())) {
            result = Color.GRAY;
        } else if ("aborted".equals(job.getColor()) || "aborted_anime".equals(job.getColor())) {
            result = Color.GRAY;
        } else {
            result = Color.GRAY;
        }

        return result.darker().darker();
    }

    private int getJobHeight(int rows) {

        int result = getCanvasHeight();
        result -= 2 * MARGIN + ((rows - 1) * JOB_MARGIN);

        return result / rows;
    }

    private String getJobText(Job job) {

        String jobText = "";

        if (job.getDescription() != null && !job.getDescription().isEmpty()) {
            jobText = job.getDescription();
        } else {
            jobText = job.getName();
        }

        return String.format("%s #%d", jobText, job.getLastBuild().getNumber());
    }

    private Font getJobTextFont(int fontSize) {
        fontSize -= 2;
        return new Font(FONT_NAME, Font.BOLD, fontSize);
    }

    private int getJobWidth(int columns) {
        int result = getCanvasWidth();

        result -= 2 * MARGIN + ((columns - 1) * JOB_MARGIN);

        return result / columns;
    }

    private Job getLongestJobName() {
        Job result = null;

        for (Job job : jobs) {
            if (result == null || getJobText(result).length() < getJobText(job).length()) {
                result = job;
            }
        }

        return result;
    }

    private Font getMessageTextFont() {
        return new Font(FONT_NAME, Font.BOLD, MESSAGE_FONT_SIZE);
    }

    private void init() {

        if (viewName == null) {
            setTitle(jenkinsUrl);
        } else {
            setTitle(String.format("%s (%s)", jenkinsUrl, viewName));
        }

        message = MESSAGE_INITIALIZING;
        initRepaintTimer();
        initWorkerTimer();
    }

    private void initDoubleClickListener() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event) {

                if (event.getClickCount() == 2 && !event.isConsumed()) {
                    event.consume();
                    toggleScreenMode();
                }
            }
        });

    }

    private void initFrame() {

        setPreferredSize(new Dimension(600, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initDoubleClickListener();
        setVisible(true);
        pack();
    }

    private void initRepaintTimer() {

        Timer repaintTimer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                WallDisplayFrame.this.repaint();
            }
        });

        repaintTimer.start();
    }

    private void initWorkerTimer() {

        Timer workerTimer = new Timer(15000, new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                if (hudsonWorker == null) {
                    hudsonWorker = new JenkinsWorker(jenkinsUrl, viewName);
                    hudsonWorker.execute();
                }

                if (hudsonWorker.isDone()) {

                    jobs.clear();

                    try {
                        if (hudsonWorker.getException() != null) {
                            message = hudsonWorker.getException().getMessage();
                        } else {
                            jobs.addAll(JenkinsWorker.getJobsToDisplay(hudsonWorker.get(), viewName));
                            Collections.sort(jobs, new JobComperator());
                        }

                    } catch (Exception e) {
                        message = e.getMessage();
                    }

                    hudsonWorker = new JenkinsWorker(jenkinsUrl, viewName);
                    hudsonWorker.execute();
                }

            }
        });

        workerTimer.start();
    }

    /** {@inheritDoc} */
    @Override
    public void paint(Graphics graphics) {

        bufferedImage = createImage(this.getSize().width, this.getSize().height);
        bufferedGraphics = (Graphics2D) bufferedImage.getGraphics();

        bufferedGraphics.setColor(getBackground());
        bufferedGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
        bufferedGraphics.setColor(getForeground());

        bufferedGraphics.setColor(BACKGROUND_COLOR);
        bufferedGraphics.fillRect(0, 0, getWidth(), getHeight());

        if (!jobs.isEmpty()) {

            FontRenderContext frc = bufferedGraphics.getFontRenderContext();

            RenderingHints renderHints =
                    new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            bufferedGraphics.setRenderingHints(renderHints);

            Job jobLongName = getLongestJobName();

            Map<Integer, Rectangle2D> fontDimensions = new HashMap<Integer, Rectangle2D>();
            for (int i = 12; i <= 64; i++) {
                Rectangle2D fontDimension = getJobTextFont(i).getStringBounds(jobLongName.getName(), frc);
                fontDimensions.put(i, fontDimension);
            }

            int maxDimension = jobs.size();
            if (maxDimension % 2 != 0) {
                maxDimension++;
            }

            int maxFontHeight = 0;
            int maxFontHeightColumns = 0;
            int maxFontSize = 0;

            for (int columns = 1; columns <= maxDimension; columns++) {

                for (Map.Entry<Integer, Rectangle2D> entry : fontDimensions.entrySet()) {

                    double width = (MARGIN * 2) + ((columns - 1) * JOB_MARGIN) + columns * entry.getValue().getWidth();

                    int rows = (int) Math.ceil(maxDimension / columns);
                    double height = (MARGIN * 2) + ((rows - 1) * JOB_MARGIN) + rows * entry.getValue().getHeight();

                    if (width > getCanvasWidth() || height > getCanvasHeight()) {
                        break;
                    } else {
                        if (entry.getValue().getHeight() > maxFontHeight) {
                            maxFontHeight = (int) entry.getValue().getHeight();
                            maxFontHeightColumns = columns;
                            maxFontSize = entry.getKey();
                        }
                    }
                }
            }

            int columns = maxFontHeightColumns;
            int rows = (int) Math.ceil(maxDimension / columns);

            paintJobs(rows, columns, maxFontSize - 4, bufferedGraphics);

        } else {

            if (message != null && !message.isEmpty()) {
                drawMessage(bufferedGraphics);
            }
        }

        graphics.drawImage(bufferedImage, 0, 0, this);
    }

    private void paintJobs(int rows, int columns, int fontSize, Graphics2D graphics) {

        int jobWidth = (int) getJobWidth(columns);
        int jobHeight = (int) getJobHeight(rows);

        int jobIndex = 0;
        int jobX = getCanvasLeft() + MARGIN;
        for (int column = 1; column <= columns; column++) {

            int jobY = getCanvasTop() + MARGIN;
            for (int row = 1; row <= rows; row++) {

                if (jobIndex < jobs.size()) {

                    Job job = jobs.get(jobIndex);
                    Font jobTextFont = getJobTextFont(fontSize);
                    String jobText = getJobText(job);
                    Color jobColor = getJobColor(job);

                    graphics.setColor(jobColor);
                    graphics.fill(new RoundRectangle2D.Double(jobX, jobY, jobWidth, jobHeight, JOB_ARC_WIDTH, JOB_ARC_HEIGHT));

                    if (job.getLastBuild().getBuilding()) {
                        paintProgress(graphics, jobX, jobY, jobWidth, jobHeight, job);
                    } else if (job.getQueuePosition() != null) {
                        paintQueuePosition(graphics, jobX, jobY, jobWidth, jobHeight, job);
                    }

                    // center job text
                    FontMetrics fm = graphics.getFontMetrics(jobTextFont);
                    Rectangle2D textsize = fm.getStringBounds(jobText, graphics);
                    int xFontOffset = (int) ((jobWidth - textsize.getWidth()) / 2);
                    int yFontOffset = (int) (jobHeight - textsize.getHeight()) / 2 + fm.getAscent();

                    // draw job text
                    graphics.setColor(JOB_TEXT_COLOR);
                    graphics.setFont(jobTextFont);
                    graphics.drawString(jobText, jobX + xFontOffset, jobY + yFontOffset);

                    jobIndex++;
                }

                jobY += jobHeight + JOB_MARGIN;
            }

            jobX += jobWidth + JOB_MARGIN;
        }
    }

    private void paintProgress(Graphics2D graphics, int jobX, int jobY, int jobWidth, int jobHeight, Job job) {

        Color jobColor = getJobColor(job);
        Area jobRectangleArea =
                new Area(new RoundRectangle2D.Double(jobX, jobY, jobWidth, jobHeight, JOB_ARC_WIDTH, JOB_ARC_HEIGHT));

        long currentDuration = System.currentTimeMillis() - job.getLastBuild().getTimestamp();
        long lastDuration = job.getLastSuccessfulBuild().getDuration();
        long percentage = currentDuration / (lastDuration / 100);

        // draw job percentage
        double percentageWidth = jobWidth / 100 * (100 - percentage);
        Area jobPercentageRectangleArea =
                new Area(new Rectangle2D.Double(jobX + jobWidth - percentageWidth, jobY, jobX + jobWidth, jobHeight));

        jobRectangleArea.subtract(jobPercentageRectangleArea);
        graphics.setColor(jobColor.brighter().brighter());
        graphics.fill(jobRectangleArea);
    }

    private void paintQueuePosition(Graphics2D graphics, int jobX, int jobY, int jobWidth, int jobHeight, Job job) {

        int queuePosition = job.getQueuePosition();

        if (job.getQueuePosition() > MAX_QUEUE_POSITIONS) {
            queuePosition = MAX_QUEUE_POSITIONS;
        }

        graphics.setColor(QUEUE_POSITIONS_COLOR);

        int queuePositionDiameter = jobHeight / 5;
        int queuePositionX = jobX + jobWidth - queuePositionDiameter * 2;
        int queuePositionIncrement = jobHeight / (queuePosition + 1);

        int queuePositionY = jobY;
        for (int i = 1; i <= queuePosition; i++) {
            queuePositionY += queuePositionIncrement;
            graphics.fillOval(queuePositionX, queuePositionY - (queuePositionDiameter / 2), queuePositionDiameter,
                    queuePositionDiameter);
        }
    }

    private void toggleScreenMode() {

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environment.getDefaultScreenDevice();

        if (device.getFullScreenWindow() == null) {
            setVisible(false);
            dispose();
            setUndecorated(true);
            setResizable(false);
            device.setFullScreenWindow(this);
            setVisible(true);
        } else {
            setVisible(false);
            dispose();
            setUndecorated(false);
            setResizable(true);
            device.setFullScreenWindow(null);
            setVisible(true);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args1) {

        String[] args = new String[] {"http://109.239.57.117:8080/jenkins", "test1"};
        
        if (args.length == 1) {
            new WallDisplayFrame(args[0]);
        } else if (args.length == 2) {
            new WallDisplayFrame(args[0], args[1]);
        } else {
            new WallDisplayFrame();
        }
    }
}
