package de.pellepelster.jenkins.walldisplay.model;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 *
 * @author pelle
 */
public class PropertyConverter  implements Converter {
    
    
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        // we wont prodcue any api xml
    }

    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        
        JobProperty jobProperty = null;
        
        if (reader.hasMoreChildren())
        {
            reader.moveDown();
            
            jobProperty = new JobProperty();
            jobProperty.setValue(reader.getValue());
            jobProperty.setName(reader.getNodeName());
        }
        
        
        return jobProperty;
    }

    public boolean canConvert(Class type) {
        return type.equals(JobProperty.class);
    }
    
}
