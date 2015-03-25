function plural(text, count) {
	if (count == 0)
	{
		return "";
	}
	else
	{
		return count + " " + ((count > 1) ? text + "s" : text);
	}
}

function getJobByName(jobs, jobName)
{
	return $.grep(jobs, function(job, i) {
			return ( job.name == jobName);
	});
}

function getUserFriendlyTimespan(milliseconds) {

	var time = milliseconds / 1000;
	var seconds = Math.floor(time % 60);

	time /= 60;	
	var minutes = Math.floor(time % 60);
	
	time /= 60;
	var hours = Math.floor(time % 24);
	
	time /= 24;
	var days = Math.floor(time);

	time /= 30;
	var months = Math.floor(time);

	if (months > 0)
	{
		return plural("month", months) +  " " + plural("day", days);
	}

	if (days > 0)
	{
		return plural("day", days) +  " " + plural("hour", hours);
	}

	if (hours > 0)
	{
		return plural("hour", hours) + " " + plural("minute", minutes);
	}

	if (minutes > 0)
	{
		return plural("minute", minutes) + " " + plural("second", seconds);
	}

	return seconds + " seconds";
}

jQuery.fn.center = function () {

	this.css("position", "absolute");
	this.css("top", (($(window).height() - this.outerHeight()) / 2) + $(window).scrollTop() + "px");
	this.css("left", (($(window).width() - this.outerWidth()) / 2) + $(window).scrollLeft() + "px");

	return this;
};

function getParameterByName(name, defaultValue)
{
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.href);
	if(results == null)
		return defaultValue;
	else
		return decodeURIComponent(results[1].replace(/\+/g, " "));
}

Array.prototype.remove = function (value)
{
	for (var i = 0; i < this.length; )
	{
		if (this[i] === value)
		{
			this.splice(i, 1);
		}
		else
		{
			++i;
		}
	}
};

function removeMessage()
{
	$("#Message").remove();
};

function getLongestJob(jobs, showBuildNumber, showLastStableTimeAgo, showDetails, showJunitResults) {

	var job = null;

	$.each(jobs, function(index, currentJob) { 
		if (job == null || getJobText(currentJob, showBuildNumber, showLastStableTimeAgo, showDetails, showJunitResults).length > getJobText(job, showBuildNumber, showLastStableTimeAgo, showDetails, showJunitResults).length)
		{
			job = currentJob;
		}
	});

	return job;
}

function getJobTitle(job) {

	var jobTitle = job.name;

	if (job.displayName) {
		jobTitle = job.displayName;
	}
	
	if (job.property != null)
	{
		$.each(job.property, function(index, property) {
			if (property.wallDisplayName != null)
			{
				jobTitle = property.wallDisplayName;
			}
		});
	}

	if (job.lastBuild != null && job.lastBuild.actions != null &&	
	    job.lastBuild.actions.length > 0 && job.lastBuild.actions [0].parameters != null &&
	    job.lastBuild.actions [0].parameters.length > 0){
		var params = job.lastBuild.actions [0].parameters;
		$.each (params,  function(index, property) {
			jobTitle = jobTitle.replace ("${"+property.name +"}", property.value);
		});
    }

	return jobTitle;
}

//String format from: http://jsfiddle.net/joquery/9KYaQ/
String.format = function() {
    // The string containing the format items (e.g. "{0}")
    // will and always has to be the first argument.
    var theString = arguments[0];
    // start with the second argument (i = 1)
    for (var i = 1; i < arguments.length; i++) {
        // "gm" = RegEx options for Global search (more than one instance)
        // and for Multiline search
        var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
        theString = theString.replace(regEx, arguments[i]);
    }
    return theString;
}

function getJunitResults(job)
{
  lastBuild = job.lastBuild
  jobActions = lastBuild.actions
  appendText=""
  $.each(jobActions, function(actionIndex, action){
    if(action && action.totalCount){
      template = "<br/><small>{0} test{1} run, {2} test{3} failed</small>"
      formatedLine = String.format(template,action.totalCount, (action.totalCount > 1) ? "s" : "", action.failCount, (action.failCount > 1) ? "s" : "")
      appendText += formatedLine
    }
  });

  //junitResults = jobActions.hudson.tasks.junit.TestResultAction
  return appendText 
}

function getJobText(job, showBuildNumber, showLastStableTimeAgo, showDetails, showJunitResults) {

	var jobText = getJobTitle(job);

      if (showBuildNumber && job.lastBuild != null && job.lastBuild.number != null)
      {
          jobText += ' #' + job.lastBuild.number;
          //Get Junit results in case of there is a build number
		  
		  if (showJunitResults) {
			jobText += getJunitResults(job)
		  }
      }

	var appendText = new Array();

	if (showDetails) {
        var culprit = getCulprit(job);
        var claimer = getClaimer(job);
        if (job.color == "green" || job.color == "green_anime" || job.color == "blue" || job.color == "blue_anime") {
            if (showLastStableTimeAgo && job.lastStableBuild != null && job.lastStableBuild.timestamp != null) {
                appendText.push($.timeago(job.lastStableBuild.timestamp));
            }
        }
        else {
            if (claimer != "") {
                appendText.push(claimer);
            } else if(culprit != "") {
                appendText.push(culprit);
            };
        }

        if (job.color == "yellow" || job.color == "yellow_anime") {
            if(job.lastBuild.actions[4] != undefined && job.lastBuild.actions[4].failCount != undefined && job.lastBuild.actions[4].totalCount != undefined) {
                appendText.push(job.lastBuild.actions[4].failCount + "/" + job.lastBuild.actions[4].totalCount);
            };
        }
        
        if(appendText.length > 0) {
            jobText += " (" + appendText.join(", ") + ")";
        };
    }

	return jobText;
}

function getGravatarUrl(job, showGravatar, size, gravatarUrl) {
    if(showGravatar && getEmail(job) !== "") {
        var hash = CryptoJS.MD5(getEmail(job).toLowerCase());

        return (gravatarUrl != null && gravatarUrl != "" ? gravatarUrl : "http://www.gravatar.com/avatar/") + hash + "?s=" + size;
    }
}
function getEmail(job) {
    var email = "";

    if(job.lastBuild != null && job.lastBuild.culprits != null && job.lastBuild.culprits != "") {
       for(i in job.lastBuild.culprits[0].property) {
          email = job.lastBuild.culprits[0].property[i].address || email;
       }
    }

    return email;
}

function getCulprit(job) {
	var culprit = "";

	if(job.lastBuild != null && job.lastBuild.culprits != null && job.lastBuild.culprits != "") {
		culprit = job.lastBuild.culprits[0].fullName;
	}

	return culprit;
}

function getClaimer(job) {
    var claimer = "";

    var build = isJobBuilding(job) ? job.lastCompletedBuild : job.lastBuild;
    
    if (build && build.actions)     
        $.each(build.actions, function(actionIndex, action){
    
            if(action && action.claimed){
                claimer = action.claimedBy;
            }
        });
    return claimer;
}

function isBuildClaimed(job) {
    return getClaimer(job) != "";
}

function trim(str) {
    return str.replace(/^\s+|\s+$/g,'');
}
function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}
