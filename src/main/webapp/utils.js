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

	var time = milliseconds / 1000
	var seconds = Math.floor(time % 60)

	time /= 60	
	var minutes = Math.floor(time % 60)
	
	time /= 60
	var hours = Math.floor(time % 24)
	
	time /= 24
	var days = Math.floor(time)

	time /= 30
	var months = Math.floor(time)

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
}

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
}

function removeMessage()
{
	$("#Message").remove()
}

function getLongestJob(jobs, showDetails) {

	var job = null;

	$.each(jobs, function(index, currentJob) { 
		if (job == null || getJobText(currentJob, showDetails).length > getJobText(job, showDetails).length)
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
	
	return jobTitle;
}

function getJobText(job, showDetails) {  

	var jobText = getJobTitle(job);

	if (showDetails == "true") {
		var culprit = getCulprit(job);
		if (job.color == "yellow") {
			jobText += " (" + job.lastBuild.actions[4].failCount + "/" + job.lastBuild.actions[4].totalCount;
			if(culprit != "") {
				jobText += ", " + culprit;
			}
			jobText += ")";
		}
		if (job.color == "red") {
			if(culprit != "") {
				jobText += " (" + culprit + ")";
			}
		}		
	}
	
	return jobText;
}

function getCulprit(job) {
	var culprit = "";
		
	if(job.lastBuild != null && job.lastBuild.culprits != "") {
		culprit = job.lastBuild.culprits[0].fullName
	}
	
	return culprit;
}

function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}