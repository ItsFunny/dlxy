<script>
        var i=0;
        var timer=null;
        $(function () {
            function f1 () {
                timer=setInterval(function () {
                    i++;
                    if(i==5){
                        i=0;
                    }
                    show();
                },3000);
            }
            f1();
            function show() {
                $("#banner li").eq(i).fadeIn().siblings().fadeOut();
                $("#banner_dot li").eq(i).addClass("blue").siblings().removeClass();
            }

            $("#banner_dot li").on("mouseover",function () {
                clearInterval(timer);
                    var index=$(this).index();
                    i=index;
                    show();
                });
            $("#banner_dot li").on("mouseleave",function () {
                var index=$(this).index();
                i=index;
                f1();
                show();
            });
        })
    </script>
