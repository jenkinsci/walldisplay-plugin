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

function getLongestJob(jobs, showBuildNumber, showLastStableTimeAgo, showDetails) {

	var job = null;

	$.each(jobs, function(index, currentJob) { 
		if (job == null || getJobText(currentJob, showBuildNumber, showLastStableTimeAgo, showDetails).length > getJobText(job, showBuildNumber, showLastStableTimeAgo, showDetails).length)
		{
			job = currentJob;
		}
	});

	return job;
}

function getJobTitle(job) {

	var jobTitle = job.name;

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

function getJobText(job, showBuildNumber, showLastStableTimeAgo, showDetails) {

	var jobText = getJobTitle(job);

      if (showBuildNumber && job.lastBuild != null && job.lastBuild.number != null)
      {
          jobText += ' #' + job.lastBuild.number;
      }      

	var appendText = new Array();

	if (showDetails == true) {
        var culprit = getCulprit(job);
        var claimer = getClaimer(job);
        if ((job.color == "green" || job.color == "green_anime" || job.color == "blue" || job.color == "blue_anime") && showLastStableTimeAgo && job.lastStableBuild != null && job.lastStableBuild.timestamp != null) {
            appendText.push($.timeago(job.lastStableBuild.timestamp));
            }

        if (job.color == "yellow" || job.color == "yellow_anime") {
            if(job.lastBuild.actions[4] != undefined && job.lastBuild.actions[4].failCount != undefined && job.lastBuild.actions[4].totalCount != undefined) {
                appendText.push(job.lastBuild.actions[4].failCount + "/" + job.lastBuild.actions[4].totalCount);
            };
        }
        
        if (job.color == "red" || job.color == "red_anime" || job.color == "yellow" || job.color == "yellow_anime") {
            if (claimer != "") {
                appendText.push(claimer);
            } else if(culprit != "") {
                appendText.push(culprit);
            };
        }

        if(appendText.length > 0) {
            jobText += " (" + appendText.join(", ") + ")";
        };
    }

	return jobText;
}

function getGravatarUrl(job, showGravatar, size) {
    if(showGravatar && getEmail(job) !== "") {
        var hash = CryptoJS.MD5(getEmail(job).toLowerCase());

        return "http://www.gravatar.com/avatar/" + hash + "?s=" + size;
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
