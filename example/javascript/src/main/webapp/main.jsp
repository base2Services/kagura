<%--
  Created by IntelliJ IDEA.
  User: aubels
  Date: 2/09/13
  Time: 5:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" import="javax.servlet.jsp.PageContext" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <t:head />
    <title>Kagura</title>
</head>

<body>
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="brand" href="#">Kagura</a>
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li class="active"><a href="#" onclick="displayMain();">Home</a></li>
                        <li>
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Reports</a>
                            <ul id="reportDropdownList" class="dropdown-menu" role="menu" aria-labelledby="dLabel">
                                <li class="hidden"></li>
                            </ul>
                        </li>
                        <li><a href="#" onclick="displayAbout();return false;">About</a></li>
                        <li><a href="#" onclick="displayContactUs();return false;">Contact</a></li>
                        <li><a href="#" onclick="logout();return false;">Logout</a></li>
                    </ul>
                </div><!--/.nav-collapse -->
            </div>
        </div>
    </div>

    <div class="container">
        <div id="kaguraMain">
            <h1>Kagura Reporting Software</h1>
            <p>Hi Welcome to Kagura's Javascript template / example. If you click "Reports" above you will find a list of reports that have been configured on this server.</p>
        </div>
        <div class="hidden" id="reportMain">
            <h1 id="reportTitle"></h1>
            <p>
                <img src="" class="hidden" align="left" id="reportImage" />
                <p id="reportDescription"></p>
                <div id="reportErrors"></div>
            </p>
            <div class="row-fluid span12">
                <div class="span4" id="paramPanel">
                    <h3>Parameters</h3>
                    <div class="hidden" id="inputParamFieldTemplate">
                        <label for="inputParamTemplate"> </label> <input id="inputParamTemplate" type="text" placeholder="" class="parameterFieldInput" />
                        <p name="helpText"></p>
                        <span name="paramId" class="hidden"></span>
                    </div>
                    <button id="runReportButton" class="btn btn-primary" type="button" onclick="rerunReport();">Run report</button>
                </div>
                <div class="span8" id="reportPanel">
                    <table class="table table-hover" id="reportTable">
                        <thead id="reportTableHeader">
                            <tr class="hidden"><th class="hidden"></th></tr>
                        </thead>
                        <tbody id="reportTableBody">
                            <tr class="hidden"><td class="hidden"></td></tr>
                        </tbody>
                    </table>
                    <div class="span12">
                        <div class="span6">
                            <a href="#" onclick="exportReport('csv', false);return false;">CSV</a>
                            <a href="#" onclick="exportReport('csv', true);return false;">(all)</a>;
                            <a href="#" onclick="exportReport('xls', false);return false;">Excel</a>
                            <a href="#" onclick="exportReport('xls', true);return false;">(all)</a>;
                            <a href="#" onclick="exportReport('pdf', false);return false;">PDF</a>
                            <a href="#" onclick="exportReport('pdf', true);return false;">(all)</a>;
                        </div>
                        <div class="span6" style="text-align: right;">
                            <a href="#" onclick="prevPage();return false;">&lt;&lt; Previous;</a>
                            Page <span id="reportPageNumber">1</span>
                            <a href="#" onclick="nextPage();return false;">;Next &gt;&gt;</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="hidden" id="kaguraAbout">
            <h1>About Us</h1>
            <p>Just a simple flexible reporting framework.</p>
        </div>
        <div class="hidden" id="kaguraContactUs">
            <h1>Contact Us</h1>
            <p>.</p>
        </div>
    </div> <!-- /container -->
    <t:footer />
    <script language="JavaScript">
        loadReportListDetailed();
        if(window.location.hash) {
            storedHash = window.location.hash.substring(1);
            if (storedHash.length > 0)
            {
                loadReport(storedHash);
            }
        }

        if ("onhashchange" in window) { // event supported?
            window.onhashchange = function () {

                if (storedHash.length > 0 && storedHash != window.location.hash.substring(1))
                {
                    storedHash = window.location.hash.substring(1);
                    loadReport(storedHash);
                }
            }
        } else { // event not supported:
            storedHash = window.location.hash;
            window.setInterval(function () {
                if (window.location.hash.substring(1) != storedHash) {
                    storedHash = window.location.hash.substring(1);
                    loadReport(storedHash);
                }
            }, 100);
        }
    </script>
</body>
</html>
