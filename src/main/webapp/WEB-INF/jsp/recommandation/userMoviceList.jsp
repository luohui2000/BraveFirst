<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="ThemeBucket">
    <link rel="shortcut icon" href="#" type="image/png">
    <title>推荐</title>
    <link href="/css/style.css" rel="stylesheet">
    <link href="/css/style-responsive.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="/js/html5shiv.js"></script>
    <script src="/js/respond.min.js"></script>
    <script src="/js/module/recommandation.js"></script>


    <![endif]-->

</head>
<body class="sticky-header">
<section>
    <div class="left-side sticky-left-side">
        <!--logo and iconic logo start-->
        <div class="logo">
            <a href="index.html"><img src="/images/logo.png" alt=""></a>
        </div>
        <div class="logo-icon text-center">
            <a href="index.html"><img src="/images/logo_icon.png" alt=""></a>
        </div>
        <!--logo and iconic logo end-->
        <div class="left-side-inner">
            <ul class="nav nav-pills nav-stacked custom-nav">
                <li><a href="index.html"><i class="fa fa-home"></i> <span>推荐电影</span></a></li>
                <li><a href="index.html"><i class="fa fa-home"></i> <span>相关电影</span></a></li>

                <li class="menu-list"><a href=""><i class="fa fa-cogs"></i> <span>其他</span></a>
                    <ul class="sub-menu-list">
                        <li><a href="index.html">一、</a></li>
                        <li><a href="index.html">二、</a></li>
                        <li><a href="index.html">三、</a></li>
                        <li><a href="index.html">四、</a></li>
                        <li><a href="index.html">五、</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div class="main-content">
        <div class="wrapper">
            <div class="row">
                <div class="col-sm-12">
                    <section class="panel">
                        <form action="" class="searchform" style="left: 50px" action="index.html" method="post">
                            <input type="text" style="width: 300px" id="userId" name="userId" placeholder="请输入用户ID"/>
                            <input type="button"
                                   onclick='getUserMoviceList()'
                                   class="btn btn-danger" style="width: 50px" value="推荐"/>
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
            url: "getUserMoviceList.do?userId=" + userId,
            type: "POST",
            async: false,
            cache: true,
            success: function (dataJson) {
                //var dataJson = $.parseJSON(dataJson);
                $("#gallery").children("div").remove();
                $.each(dataJson, function (index, item) {
                    alertString += ("用户：【" + item.user_id + "】," + "电影名称" + (index + 1) + ":【" + item.moive_name) + "】\n";

                    var str = " <div class=' audio images item '>";
                    str+="  <a target= _blank href='"+item.moive_url+"'  data-toggle='modal'>";
                    str+="  <img src='"+item.moive_img_url+"' alt='"+item.moive_name+"'/> </a>";
                    str+="  <p>"+item.moive_name+" </p></div>";
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
