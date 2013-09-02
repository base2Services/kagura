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
}
function doLogin()
{
    loginSpin();
    var user = $("#loginEmail").val();
    var password = $("#loginPassword").val();
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/rest/auth/login/" + user,
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
            }
        }
    });
}