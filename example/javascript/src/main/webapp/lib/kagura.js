/*
 Copyright 2014 base2Services

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
var opts = {
    lines: 13, // The number of lines to draw
    length: 20, // The length of each line
    width: 10, // The line thickness
    radius: 30, // The radius of the inner circle
    corners: 1, // Corner roundness (0..1)
    rotate: 0, // The rotation offset
    direction: 1, // 1: clockwise, -1: counterclockwise
    color: '#000', // #rgb or #rrggbb or array of colors
    speed: 1, // Rounds per second
    trail: 60, // Afterglow percentage
    shadow: false, // Whether to render a shadow
    hwaccel: false, // Whether to use hardware acceleration
    className: 'spinner', // The CSS class to assign to the spinner
    zIndex: 2e9, // The z-index (defaults to 2000000000)
    top: 'auto', // Top position relative to parent in px
    left: 'auto' // Left position relative to parent in px
};
var spinner = new Spinner(opts);
var token;
var pageNumber = 0;
var curReport;
var callCount = 0;
var reportTitle = null;
var storedHash;

/**
 * Starts the login spinner, installs it into #signinSection
 */
function loginSpin() {
    spinner.stop();
    $('#signinSection').find('input, button').prop("disabled", true);
    spinner.spin(document.getElementById('signinSection'));
}

/**
 * Finds and stops the spinner.
 */
function loginUnspin() {
    spinner.stop();
    $('#signinSection').find('input, button').prop("disabled", false);
}

/**
 * Stores the token into a cookie, and sets the time out for 2 days.
 * @param token
 */
function setToken(token) {
    this.token = token;
    var date = new Date();
    setCookie("token", token, date.getTime() + (2 * 24 * 60 * 60 * 1000));
}
/**
 * Changes the URL to go to "main.jsp"
 */
function gotoMain()
{
    window.location.href = server_base + "main.jsp";
}
/**
 * Attempts a login. Gets username from #loginEmail and gets password from #loginPassword.
 * If successful calls "gotoMain()" Otherwise loginUnspin() and alerts the user to the error
 */
function doLogin()
{
    loginSpin();
    var user = $("#loginEmail").val();
    var password = $("#loginPassword").val();
    $.ajax({
        type: "POST",
        url: server_base + "rest/auth/login/" + user,
        data: password,
        cache: false,
        contentType: "TEXT/PLAIN",
        success: function (msg)
        {
            loginUnspin();
            if (msg.error != "")
            {
                alert(msg.error);
            } else {
                setToken(msg.token);
                gotoMain();
            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown)
    {
        loginUnspin();
        alert(errorThrown);
    });
}
/**
 * Loads the report content list, as a simple list. (List of reports with no details.)
 * With data when it gets it back calls: loadReportListData
 */
function loadReportListSimple() {
    token = getCookie("token");
    $.ajax({
        type: "GET",
        cache: false,
        url: server_base + "rest/auth/reports/" + token,
        success: function (msg)
        {
            if (!msg.error)
            {
                loadReportListData(msg);
            } else {
                ajaxFail(null, null, msg.error);
            }
        }
    }).fail(ajaxFail);
};
/**
 * Loads the report content list, as a detailed list with parameter values and more.
 * With data when it gets it back calls: loadReportListData
 */
function loadReportListDetailed() {
    token = getCookie("token");
    $.ajax({
        type: "GET",
        cache: false,
        url: server_base + "rest/auth/reportsDetails/" + token + "/",
        success: function (msg)
        {
            if (!msg.error)
            {
                loadReportListData(msg);
            } else {
                ajaxFail(null, null, msg.error);
            }
        }
    }).fail(ajaxFail);
};
/**
 * Once you obtain the list of reports, this function populates the UI with the correct data
 * It looks for #reportDropdownList, removes all non-hidden child nodes from it. Clones the hidden node. And populates
 * the data into it as appropriate.
 * @param data
 */
function loadReportListData(data)
{
    var reportDDList = $("#reportDropdownList");
    reportDDList.find("li:not(.hidden)").remove();
    if (Array.isArray(data)) // If it is a simple list, just list it
    {
        data.forEach(function (report)
        {
            var template = reportDDList.find("li.hidden").clone();
            template.removeClass("hidden");
            reportDDList.append(template);
            template.html("<a href='#' onclick='loadReport(\""+report+"\");return false;'>"+report+"</a>");
        });
    } else // Otherwise complicated rendering.
    {
        var directoryHash = new Array();
        Object.keys(data).forEach(function (reportId)
        {
            var template = reportDDList.find("li.hidden").clone();
            template.removeClass("hidden");
            if (data[reportId].extra && data[reportId].extra.directory)
            {
                if (!directoryHash[data[reportId].extra.directory])
                {
                    directoryHash[data[reportId].extra.directory] = new Array();
                }
                directoryHash[data[reportId].extra.directory].push(template);
            } else {
                reportDDList.append(template);
            }
            var andRun = data[reportId].extra && data[reportId].extra.autorun == "no" ? "false" : "true";
            if (data[reportId].extra && data[reportId].extra.reportName)
            {
                template.html("<a href='#' onclick='reportTitle = this.text; loadReport(\""+reportId+"\", "+andRun+");return false;'>"+data[reportId].extra.reportName+"</a>");
            } else {
                template.html("<a href='#' onclick='loadReport(\""+reportId+"\", "+andRun+");return false;'>"+reportId+"</a>");
            }
        });
        var dhKeys = Object.keys(directoryHash);
        dhKeys.sort();
        dhKeys.forEach(function (key)
        {
            var template = reportDDList.find("li.hidden").clone();
            template.removeClass("hidden");
            template.addClass("dropdown-submenu");
            template.attr("role","menu");
            template.append ('<a href="#">'+key+'</a>' +
                '   <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">' +
                '   </ul>');
            var templateUl = $('ul', template);
            directoryHash[key].forEach(function (each){ templateUl.append(each);});
            reportDDList.append(template);
        });
    }
}
/**
 * Hides all categories, such as the main view, contact us view, about view and report view.
 * Looks for: #kaguraMain,#kaguraContactUs,#kaguraAbout,#reportMain
 */
function resetDisplay() {
    $('#kaguraMain,#kaguraContactUs,#kaguraAbout,#reportMain').addClass("hidden");
}
/**
 * Resets report table data & columns, and parameter side bar.
 * Looks for: #reportTableHeader, #reportTableBody, #reportTableBody, #paramPanel
 */
function resetReportConfig()
{
    $('#reportTableHeader>tr:not(.hidden)').remove();
    $('#reportTableBody>tr:not(.hidden)').remove();
    $('#reportTableBody>tr>td:not(.hidden)').remove();
    $('#paramPanel>div:not(.hidden)').remove();
}
/**
 * Resets report table data
 * Looks for: #reportTableBody
 */
function resetReportBody()
{
    $('#reportTableBody>tr:not(.hidden)').remove();
}
/**
 * Resets report table data & columns.
 * Looks for: #reportTableHeader, #reportTableBody, #reportTableBody
 */
function resetReport()
{
    $('#reportTableHeader>tr:not(.hidden)').remove();
    $('#reportTableBody>tr:not(.hidden)').remove();
    $('#reportTableBody>tr>td:not(.hidden)').remove();
}

/**
 * Adds columns to the report.
 * Expects to see: #reportTableHeader, #reportTableBody
 * @param columns
 */
function addColumns(columns)
{
    var reportTableHeader = $("#reportTableHeader");
    var reportTableBody = $("#reportTableBody");
    var templateTr = reportTableHeader.find("tr").clone();
    templateTr.removeClass("hidden");
    reportTableHeader.append(templateTr);
    columns.forEach(function (column)
    {
        var templateTh = templateTr.find("th.hidden").clone();
        if (column.label)
            templateTh.text(column.label);
        else
            templateTh.text(column.name);
        templateTh.removeClass("hidden");
        templateTr.append(templateTh);
        var templateRow = reportTableBody.find("tr.hidden");
        templateRow.removeClass("hidden");
        var templateTd = templateRow.find("td.hidden").clone();
        templateTd.removeClass("hidden");
        templateRow.append(templateTd);
        templateTd.attr("name", column.name);
        if (column.extraOptions)
        {
            if (column.extraOptions.styleType == "text")
            {
            } else if (column.extraOptions.styleType == "numbers")
            {
            }
        }
        templateRow.addClass("hidden");
    });
}
/**
 * From the report data structure builds up the parameter input. Mostly pre-written components which are inserted.
 * Looks for: #inputParamFieldTemplate, #runReportButton
 * @param msg
 * @param inputParamFieldTemplate
 */
function buildReportParameters(msg, inputParamFieldTemplate) {
    var inputParamFieldTemplate = $('#inputParamFieldTemplate');
    msg.params.forEach(function (param) {
        var template = inputParamFieldTemplate.clone();
        template.removeAttr("id");
        template.removeClass("hidden");
        template.addClass("parameterField");
        var label = template.find("span[name='inputParamLabel']");
        label.text(param.name);
        var requiredLabel = template.find("span[name='requiredLabel']");
        if (param.required)
        {
            requiredLabel.removeClass("hidden");
        }
        var paramIdField = template.find("span[name='paramId']");
        paramIdField.text(param.id);
        var paramId = param.id + "Param";
        label.prop("for", paramId);
        var input = template.find("input");
        if ("number" == param.type.toLowerCase()) {
            input.prop("id", paramId);
            input.prop("type", "number");
            input.prop("step", "any");
            input.val(param.value);
            input.prop("placeholder", param.placeholder);
            input.addClass("parameterFieldInput");
        } else if ("boolean" == param.type.toLowerCase()) {
            var options = "<option value='' " + (param.value == "" ? "selected" : "") + ">Select one</option>" +
                "<option value='true' " + (param.value == "true" ? "selected" : "") + ">Yes</option>" +
                "<option value='false' " + (param.value == "false" ? "selected" : "") + ">No</option>";
            input.replaceWith("<select id='" + paramId + "' class=\"parameterFieldInput\">" + options + "</select>");
        } else if ("combo" == param.type.toLowerCase()) {
            var options = "<option value='' " + (param.value == "" ? "selected" : "") + ">Select one</option>";
            if (param.values)
            {
                param.values.forEach(function (value) {
                    options = options + "<option value='" + value + "' " + (param.value == value ? "selected" : "") + ">" + value + "</option>";
                });
            }
            input.replaceWith("<select id='" + paramId + "' class=\"parameterFieldInput\">" + options + "</select>");
        } else if ("datetime" == param.type.toLowerCase()) {
            input.replaceWith('    <div id="' + paramId + '" class="input-append date">' +
                '<input type="text" class="parameterFieldInput" placeholder="' + param.placeholder + '" />' +
                '<span class="add-on">' +
                '<i data-time-icon="icon-time" data-date-icon="icon-calendar"></i>' +
                '</span>' +
                '</div>' +
                '<script type="text/javascript">' +
                '$(function() {' +
                '$("#' + paramId + '").datetimepicker({' +
                "        format: 'yyyy-MM-dd hh:mm:ss' " +
                '});' +
                '});' +
                '</script>');
        } else if ("date" == param.type.toLowerCase()) {
            input.replaceWith('    <div id="' + paramId + '" class="input-append date">' +
                '<input type="text" class="parameterFieldInput" placeholder="' + param.placeholder + '" />' +
                '<span class="add-on">' +
                '<i data-time-icon="icon-time" data-date-icon="icon-calendar"></i>' +
                '</span>' +
                '</div>' +
                '<script type="text/javascript">' +
                '$(function() {' +
                '$("#' + paramId + '").datetimepicker({' +
                "        format: 'yyyy-MM-dd', " +
                '        pickTime: false' +
                '});' +
                '});' +
                '</script>');
        } else if ("manycombo" == param.type.toLowerCase()) {
            var options = "<option disabled='true' value=''>Select one or more</option>";
            if (param.values)
            {
                param.values.forEach(function (value) {
                options = options + "<option value='" + value + "' " + ((param.value != null && param.value.contains(value)) ? "selected" : "") + ">" + value + "</option>";
                });
            }
            input.replaceWith("<select id='" + paramId + "' class=\"parameterFieldInput\" multiple>" + options + "</select>");
        } else //if ("string" == param.type.toLowerCase())
        {
            input.prop("id", paramId);
            input.val(param.value);
            input.prop("placeholder", param.placeholder);
            input.addClass("parameterFieldInput");
        }
        var help = template.find("p[name='help']");
        help.text(param.help);
        template.insertBefore('#runReportButton');
    });
}
/**
 * Handles special extraOptions on the report.
 * Looks for: #reportTitle, #reportDescription, #reportImage
 * @param msg
 * @param reportId
 */
function processReportExtras(msg, reportId) {
    if (msg.extra.reportName) {
        $("#reportTitle").text(msg.extra.reportName);
    } else {
        $("#reportTitle").text(reportId);
    }
    if (msg.extra.description) {
        $("#reportDescription").text(msg.extra.description);
    } else {
        $("#reportDescription").text("");
    }
    var reportImage = $("#reportImage");
    if (msg.extra.image) {
        reportImage.prop('src', msg.extra.image);
        reportImage.removeClass("hidden");
    } else {
        reportImage.addClass("hidden");
    }
}
/**
 * Load report details.
 * @param reportId
 */
function loadReport(reportId)
{
    loadReport(reportId, true);
}
/**
 * Load and optionally run a report.
 * @param reportId
 * @param andRun
 */
function loadReport(reportId, andRun)
{
    if (reportId == null || reportId == "") { return; }
    var url = server_base + "rest/report/" + token + "/" + reportId + "/details";
    if (andRun == true)
    {
        url = server_base + "rest/report/" + token + "/" + reportId + "/detailsAndRun";
    }
    var method = "GET";
    var contentType = "application/json; charset=utf-8";
    storedHash = reportId;
    window.location.hash = "#" + reportId;
    curReport = null;
    $(".alert").alert('close');
    callKagura(reportId, method, url, contentType)
}

/**
 * Display the main section , after resetting the view.
 * Looks for: #kaguraMain
 */
function displayMain()
{
    resetDisplay();
    $('#kaguraMain').removeClass("hidden");
}
/**
 * Displays the contact us section, after resetting the view.
 */
function displayContactUs()
{
    resetDisplay();
    $('#kaguraContactUs').removeClass("hidden");
}
/**
 * Displays the about us section, after resetting the view.
 */
function displayAbout()
{
    resetDisplay();
    $('#kaguraAbout').removeClass("hidden");
}
/**
 * Starts a download of the report int he desired format
 * @param fileType
 * @param all
 */
function exportReport(fileType, all)
{
    var values = "";
    if (all)
    {
        values = values + "&allpages=true";
    } else {
        values = values + "&page=" + pageNumber;
    }
    values = values + "&parameters=" + buildRequestParameters();
    window.location.href = server_base + "rest/report/" + token + "/" + curReport + "/export."+fileType+"?" + values;
}

/**
 * Compiles a JSON string of the report parameters, then URI encodes it.
 * Looks for: .parameterFieldInput
 * @returns {string}
 */
function buildRequestParameters() {
    var params = {};
    $('div.parameterField:not(.hidden)').each(function (index, divfield) {
        var param = $(divfield).find("span[name='paramId']").text();
        var value = $(divfield).find(".parameterFieldInput").val();
        params[param] = value;
    });
    var encParams = encodeURIComponent(JSON.stringify(params));
    return encParams;
}
/**
 * Reruns the report, resetting the page number
 */
function rerunReport()
{
    pageNumber = 0;
    runReport();
}
/**                                                            4
 * Writes a bootstrap alert for errors returned in the message.
 * Looks for: #reportErrors
 * @param msg
 */
function reportErrors(msg) {
    msg.errors.forEach(function (error) {
        var newDiv = $('<div></div>');
        newDiv.html('<div class="alert">' +
            '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
            '<strong>Warning!</strong> ' + error +
            '</div>');
        $('#reportErrors').append(newDiv);
    })
}
/**
 * Populates the table.
 * Looks for: #reportTableBody
 * @param msg
 */
function populateReportRows(msg) {
    var reportTableBody = $("#reportTableBody");
    var templateRow = reportTableBody.find("tr.hidden");
    msg.rows.forEach(function (row) {
        var rowInstance = templateRow.clone();
        rowInstance.removeClass("hidden");
        reportTableBody.append(rowInstance);
        rowInstance.find("td.hidden").remove();
        Object.keys(row).forEach(function (key) {
            rowInstance.find("td[name='" + key + "']").text(row[key]);
        })
    });
}
/**
 * Updates column with new column details if changed.
 * @param msg
 */
function fixReportColumns(msg) {
    if (!msg.rows || msg.rows.length == 0) {
        resetReportBody();
    } else {
        resetReport();
        var columns = $.map(Object.keys(msg.rows[0]), function (x) {
            return { name: x, styleName: ""};
        });
        addColumns(columns);
    }
}
/**
 * Runs the report.
 */
function runReport()
{
    var values = "page=" + pageNumber;
    var encParams = buildRequestParameters();
    var url = server_base + "rest/report/" + token + "/" + curReport + "/run?" + values + "&parameters=" + encParams;
    var method = "GET";
    var contentType = null;
    callKagura(curReport, method, url, contentType)
}

/**
 * Ajax call to restful services, then executes the appopriate functions based on the structure of the response.
 * Requires tags: #reportPageNumber, #reportMain, #reportTitle
 * @param reportId
 * @param method
 * @param url
 * @param contentType
 */
function callKagura(reportId, method, url, contentType)
{
    // If we have changed report, reset everything.
    if (reportId != curReport)
    {
        resetDisplay();
        resetReportConfig();
        if (reportTitle != null)
        {
            $('#reportTitle').text(reportTitle);
        } else {
            $('#reportTitle').text(reportId);
        }
        var reportMain = $('#reportMain');
        reportMain.removeClass("hidden");
    } else {
        resetReportBody();
    }
    spinner.stop();
    spinner.spin(document.getElementById('reportMain'));
    $('#reportPageNumber').text(pageNumber+1);
    resetReportBody();
    var myCall = ++callCount;
    $.ajax({
        type: method,
        url: url,
        cache: false,
        contentType: contentType,
        timeout: 600000,
        success: function (msg)
        {
            if (myCall != callCount) return; // Damn Double Clickers
            if (msg.error)
            {
                ajaxFail(null, null, msg.error);
                spinner.stop();
                return;
            }
            if (msg.errors)
            {
                reportErrors(msg);
            }
            if (msg.extra)
            {
                processReportExtras(msg, reportId);
            }
            if (msg.params)
            {
                buildReportParameters(msg);
            }
            if (msg.columns)
            {
                if (reportId != curReport)
                {
                    addColumns(msg.columns);
                }
            } else {
                fixReportColumns(msg);
            }
            if (msg.rows)
            {
                populateReportRows(msg);
            }
            if (reportId != curReport)
            {
                pageNumber = 0;
                curReport = reportId;
            }
            spinner.stop();
        }
    }).fail(ajaxFail);
}

/**
 * Decrements the page number then runs the report again
 */
function prevPage()
{
    pageNumber = Math.max(pageNumber - 1, 0);
    runReport();
}

/**
 * Increments the page number then runs the report again
 */
function nextPage()
{
    pageNumber = pageNumber + 1;
    runReport();
}

/**
 * Logouts, removes token, and resets page.
 */
function logout()
{
    setCookie("token", "",1);
    window.location = server_base + "index.jsp";
}

/**
 * Reports failures to the user. Used in AJAX calls.
 * @param jqXHR
 * @param textStatus
 * @param errorThrown
 */
function ajaxFail(jqXHR, textStatus, errorThrown)
{
    if (errorThrown == "Not Found")
    {
        alert("Can not find reporting backend, please ensure report server is running.");
    } else if (errorThrown == "Authentication failure")
    {
        alert("Session expired.");
        logout();
    } else {
        alert(errorThrown);
    }
}