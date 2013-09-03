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
            loadReportListData(msg);
        }
    }).fail(function (jqXHR, textStatus, errorThrown)
        {
            alert(errorThrown);
        });
};
function loadReportListData(data)
{
    var reportDDList = $("#reportDropdownList");
    reportDDList.find("li:not(.hidden)").remove();
//    Object.keys(data)
    data.forEach(function (report)
    {
        var template = reportDDList.find("li.hidden").clone();
        template.removeClass("hidden");
        reportDDList.append(template);
        template.html("<a href='#' onclick='loadReport(\""+report+"\")'>"+report+"</a>");
    });
}
function resetDisplay() {
    $('#kaguraMain,#reportContactUs,#kaguraAbout,#reportMain').addClass("hidden");
}
function loadReport(reportId)
{
    resetDisplay();
    $('#reportTitle').text(reportId);
    spinner.stop();
    var reportMain = $('#reportMain');
    reportMain.removeClass("hidden");
    spinner.spin(document.getElementById('reportMain'));
    $.ajax({
        type: "GET",
        url: server_base + "rest/report/" + token + "/" + reportId + "/details",
        success: function (msg)
        {
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
                reportImage.src(msg.extra.image);
                reportImage.removeClass("hidden");
            } else {
                reportImage.addClass("hidden");
            }
            spinner.stop();
        }
    }).fail(function (jqXHR, textStatus, errorThrown)
        {
            alert(errorThrown);
        });
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
