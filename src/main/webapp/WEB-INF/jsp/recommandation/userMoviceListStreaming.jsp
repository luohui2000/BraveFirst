<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="ThemeBucket">
    <link rel="shortcut icon" href="#" type="image/png">
    <title>实时推荐</title>
    <link href="/css/style.css" rel="stylesheet">
    <link href="/css/style-responsive.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="/js/html5shiv.js"></script>
    <script src="/js/respond.min.js"></script>
    <script src="/js/module/recommandation.js"></script>



    <![endif]-->

</head>
<body class="sticky-header" onload="getUserMoviceList();">

<%
    String userId = session.getAttribute("userId").toString();
%>
<section>
    <div class="left-side sticky-left-side">
        <!--logo and iconic logo start-->
        <div class="logo">
            <a href="/index.html"><img style="width: 200px;height: 50px" src="/images/logo.png" alt=""></a>
        </div>
        <div class="logo-icon text-center">
            <a href="/index.html"><img src="/images/logo_icon.png" alt=""></a>
        </div>
        <!--logo and iconic logo end-->
        <div class="left-side-inner">
            <ul class="nav nav-pills nav-stacked custom-nav">
                <li><a href="/recommandation/gotoUserMoviceList.do"><i class="fa fa-home"></i> <span>离线推荐</span></a></li>
                <li><a href="/recommandation/gotoUserMoviceListStreaming.do"><i class="fa fa-home"></i> <span>实时推荐</span></a></li>

                <li class="menu-list"><a href=""><i class="fa fa-cogs"></i> <span>其他</span></a>
                    <ul class="sub-menu-list">
                        <li><a href="/index.html">一、</a></li>
                        <li><a href="/index.html">二、</a></li>
                        <li><a href="/index.html">三、</a></li>
                        <li><a href="/index.html">四、</a></li>
                        <li><a href="/index.html">五、</a></li>
                    </ul>
                </li>
            </ul>
        </div>

    </div>
    <div class="menu-right">
        <ul class="notification-menu">
            <li>
                <a href="#" class="btn btn-default dropdown-toggle info-number" data-toggle="dropdown">
                    <i class="fa fa-tasks"></i>
                    <span class="badge">8</span>
                </a>
                <div class="dropdown-menu dropdown-menu-head pull-right">
                    <h5 class="title">You have 8 pending task</h5>
                    <ul class="dropdown-list user-list">
                        <li class="new">
                            <a href="#">
                                <div class="task-info">
                                    <div>Database update</div>
                                </div>
                                <div class="progress progress-striped">
                                    <div style="width: 40%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="40" role="progressbar" class="progress-bar progress-bar-warning">
                                        <span class="">40%</span>
                                    </div>
                                </div>
                            </a>
                        </li>
                        <li class="new">
                            <a href="#">
                                <div class="task-info">
                                    <div>Dashboard done</div>
                                </div>
                                <div class="progress progress-striped">
                                    <div style="width: 90%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="90" role="progressbar" class="progress-bar progress-bar-success">
                                        <span class="">90%</span>
                                    </div>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <div class="task-info">
                                    <div>Web Development</div>
                                </div>
                                <div class="progress progress-striped">
                                    <div style="width: 66%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="66" role="progressbar" class="progress-bar progress-bar-info">
                                        <span class="">66% </span>
                                    </div>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <div class="task-info">
                                    <div>Mobile App</div>
                                </div>
                                <div class="progress progress-striped">
                                    <div style="width: 33%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="33" role="progressbar" class="progress-bar progress-bar-danger">
                                        <span class="">33% </span>
                                    </div>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <div class="task-info">
                                    <div>Issues fixed</div>
                                </div>
                                <div class="progress progress-striped">
                                    <div style="width: 80%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="80" role="progressbar" class="progress-bar">
                                        <span class="">80% </span>
                                    </div>
                                </div>
                            </a>
                        </li>
                        <li class="new"><a href="">See All Pending Task</a></li>
                    </ul>
                </div>
            </li>

            <li>
                <a href="#" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <img src="../images/avatar-mini.jpg" alt="" />
                    <%=userId %>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu dropdown-menu-usermenu pull-right">
                    <li><a href="#"><i class="fa fa-user"></i>  Profile</a></li>
                    <li><a href="#"><i class="fa fa-cog"></i>  Settings</a></li>
                    <li><a href="/recommandation/loginOut.do" ><i class="fa fa-sign-out"></i> Log Out</a></li>
                </ul>
            </li>

        </ul>
    </div>
    <div class="main-content">
        <div class="wrapper">
            <div class="row">
                <div class="col-sm-12">
                    <section class="panel">
                        <form action="" class="searchform" style="left: 50px" action="index.html" method="post">
                            <input type="text" style="width: 300px" id="userId" readonly name="userId" value="<%=userId %>" placeholder="请输入用户ID"/>
                            <input type="button"
                                   onclick='getUserMoviceList()'
                                   class="btn btn-danger" style="width: 100px" value="实时推荐"/>
                        </form>
                        <div class="panel-body">
                            <div id="gallery" class="media-gal">

                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
    <!-- main content end-->
</section>

<!-- Placed js at the end of the document so the pages load faster -->
<script src="/js/jquery-1.10.2.min.js"></script>

<script src="/js/bootstrap.min.js"></script>
<script src="/js/modernizr.min.js"></script>
<script>
    function getUserMoviceList() {
        var userId = $("#userId").val();
        var alertString = "推荐结果：\n\n";
        $.ajax({
            url: "getUserMoviceListStreaming.do?userId=" + userId,
            type: "POST",
            async: false,
            cache: true,
            success: function (dataJson) {
                //var dataJson = $.parseJSON(dataJson);
                $("#gallery").children("div").remove();
                $.each(dataJson, function (index, item) {
                    alertString += ("用户：【" + item.user_id + "】," + "电影名称" + (index + 1) + ":【" + item.movie_name) + "】\n";
                    var str = " <div class=' audio images item '>";
                    str+="  <a target= _blank href='"+item.movie_url+"'  data-toggle='modal'>";
                    str+="  <img src='"+item.movie_img_url+"' alt='"+item.movie_name+"'/> </a>";
                    str+="  <p>"+item.movie_name+" </p></div>";
                    $("#gallery").prepend(str)
                });
               // alert(alertString);

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }


</script>
</body>
</html>
