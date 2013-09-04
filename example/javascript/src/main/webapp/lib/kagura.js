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
var pageNumber = 1;
var curReport;

function loginSpin() {
    spinner.stop();
    $('#signinSection').find('input, button').prop("disabled", true);
    spinner.spin(document.getElementById('signinSection'));
}

function loginUnspin() {
    spinner.stop();
    $('#signinSection').find('input, button').prop("disabled", false);
}

function setToken(token) {
    this.token = token;
    var date = new Date();
    setCookie("token", token, date.getTime() + (2 * 24 * 60 * 60 * 1000));
}
function gotoMain()
{
    window.location.href = server_base + "main.jsp";
}
function doLogin()
{
    loginSpin();
    var user = $("#loginEmail").val();
    var password = $("#loginPassword").val();
    $.ajax({
        type: "POST",
        url: server_base + "rest/auth/login/" + user,
        data: password,
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
function loadReportList() {
    token = getCookie("token");
    $.ajax({
        type: "GET",
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
function loadReportListData(data)
{
    var reportDDList = $("#reportDropdownList");
    reportDDList.find("li:not(.hidden)").remove();
    data.forEach(function (report)
    {
        var template = reportDDList.find("li.hidden").clone();
        template.removeClass("hidden");
        reportDDList.append(template);
        template.html("<a href='#' onclick='loadReport(\""+report+"\")'>"+report+"</a>");
    });
}
function resetDisplay() {
    $('#kaguraMain,#kaguraContactUs,#kaguraAbout,#reportMain').addClass("hidden");
}
function resetReportConfig()
{
    $('#reportTableHeader>tr:not(.hidden)').remove();
    $('#reportTableBody>tr:not(.hidden)').remove();
    $('#reportTableBody>tr>td:not(.hidden)').remove();
    $('#paramPanel>div:not(.hidden)').remove();
}
function resetReport()
{
    $('#reportTableBody>tr:not(.hidden)').remove();
}
function loadReport(reportId)
{
    resetDisplay();
    resetReportConfig();
    resetReport();
    $('#reportTitle').text(reportId);
    spinner.stop();
    var reportMain = $('#reportMain');
    reportMain.removeClass("hidden");
    spinner.spin(document.getElementById('reportMain'));
    $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: server_base + "rest/report/" + token + "/" + reportId + "/details",
        success: function (msg)
        {
            if (msg.error)
            {
                ajaxFail(null, null, msg.error);
            }
            if (msg.extra.reportName)
            {
                $("#reportTitle").text(msg.extra.reportName);
            }
            if (msg.extra.description)
            {
                $("#reportDescription").text(msg.extra.description);
            }
            var reportImage = $("#reportImage");
            if (msg.extra.image)
            {
                reportImage.prop('src', msg.extra.image);
                reportImage.removeClass("hidden");
            } else {
                reportImage.addClass("hidden");
            }
            var reportTableHeader = $("#reportTableHeader");
            var reportTableBody = $("#reportTableBody");
            var templateTr = reportTableHeader.find("tr").clone();
            templateTr.removeClass("hidden");
            reportTableHeader.append(templateTr);
            msg.columns.forEach(function (column)
            {
                var templateTh = templateTr.find("th.hidden").clone();
                templateTh.text(column.name);
                templateTh.removeClass("hidden");
                templateTr.append(templateTh);
                var templateRow = reportTableBody.find("tr.hidden");
                templateRow.removeClass("hidden");
                var templateTd = templateRow.find("td.hidden").clone();
                templateTd.removeClass("hidden");
                templateRow.append(templateTd);
                templateTd.attr("name", column.name);
                if (column.styleType == "text")
                {
                } else if (column.styleType == "numbers")
                {
                }
                templateRow.addClass("hidden");
            });
            var paramPanel = $('#paramPanel');
            var inputParamFieldTemplate = $('#inputParamFieldTemplate');
            msg.params.forEach(function (param)
            {
                var template = inputParamFieldTemplate.clone();
                template.removeAttr("id");
                template.removeClass("hidden");
                template.addClass("parameterField");
                var label = template.find("label");
                label.text(param.name);
                var paramIdField = template.find("span[name='paramId']");
                paramIdField.text(param.id);
                var paramId = param.id + "Param";
                label.prop("for", paramId);
                var input = template.find("input");
                if ("number" == param.type.toLowerCase())
                {
                    input.prop("id", paramId);
                    input.prop("type", "number");
                    input.val(param.value);
                    input.prop("placeholder", param.placeholder);
                    input.addClass("parameterFieldInput");
                } else if ("boolean" == param.type.toLowerCase())
                {
                    var options = "<option value='' "+(param.value == "" ? "selected" : "")+">Select one</option>" +
                        "<option value='true' "+(param.value == "true" ? "selected" : "")+">Yes</option>" +
                        "<option value='false' "+(param.value == "false" ? "selected" : "")+">No</option>";
                    input.replaceWith("<select id='"+paramId+"' class=\"parameterFieldInput\">"+options+"</select>");
                } else if ("combo" == param.type.toLowerCase())
                {
                    var options = "<option value='' "+(param.value == "" ? "selected" : "")+">Select one</option>";
                    param.values.forEach(function (value)
                    {
                        options = options + "<option value='"+value+"' "+(param.value == value ? "selected" : "")+">"+value+"</option>";
                    });
                    input.replaceWith("<select id='"+paramId+"' class=\"parameterFieldInput\">"+options+"</select>");
                } else if ("manycombo" == param.type.toLowerCase())
                {
                    var options = "<option value='' "+(param.value == null || param.value.length == 0 ? "selected" : "")+">Select one</option>";
                    param.values.forEach(function (value)
                    {
                        options = options + "<option value='"+value+"' "+((param.value != null  && param.value.contains(value)) ? "selected" : "")+">"+value+"</option>";
                    });
                    input.replaceWith("<select id='"+paramId+"' class=\"parameterFieldInput\" multiple>"+options+"</select>");
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
            spinner.stop();
            pageNumber = 1;
            curReport = reportId;
        }
    }).fail(ajaxFail);
}
function displayMain()
{
    resetDisplay();
    $('#kaguraMain').removeClass("hidden");
}
function displayContactUs()
{
    resetDisplay();
    $('#kaguraContactUs').removeClass("hidden");
}
function displayAbout()
{
    resetDisplay();
    $('#kaguraAbout').removeClass("hidden");
}
function exportReport(fileType, all)
{
    var values = "page=" + pageNumber;
    if (all)
    {
        values = values + "&allpages=true";
    } else {
        values = values + "&page=" + pageNumber;
    }
    window.open(server_base + "rest/report/" + token + "/" + curReport + "/export."+fileType+"?" + values);
}

function runReport()
{
    resetReport();
    spinner.stop();
    spinner.spin(document.getElementById('reportMain'));
    $('#reportPageNumber').text(pageNumber);
    var values = "page=" + pageNumber;
    var params = {};
    $('div.parameterField:not(.hidden)').each(function (index, divfield) {
        var param = $(divfield).find("span[name='paramId']").text();
        var value = $(divfield).find(".parameterFieldInput").val();
        params[param] = value;
    });
    var encParams = encodeURIComponent(JSON.stringify(params));
    resetReport();
    $.ajax({
        type: "GET",
        url: server_base + "rest/report/" + token + "/" + curReport + "/run?" + values + "&parameters=" + encParams,
        success: function (msg)
        {
            var reportTableBody = $("#reportTableBody");
            var templateRow = reportTableBody.find("tr.hidden");
            msg.rows.forEach(function (row)
            {
                var rowInstance = templateRow.clone();
                rowInstance.removeClass("hidden");
                reportTableBody.append(rowInstance);
                rowInstance.find("td.hidden").remove();
                Object.keys(row).forEach(function (key)
                {
                    rowInstance.find("td[name='"+key+"']").text(row[key]);
                })
            });
            spinner.stop();
        }
    }).fail(ajaxFail);
}

function prevPage()
{
    pageNumber = Math.max(pageNumber - 1, 1);
    runReport();
}

function nextPage()
{
    pageNumber = pageNumber + 1;
    runReport();
}

function logout()
{
    setCookie("token", "",1);
    window.location = server_base + "index.jsp";
}

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