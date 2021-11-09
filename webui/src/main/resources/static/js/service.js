$(function () {
    setInterval(function () {
        jQuery.ajaxSetup ({cache:false});
        // window.location.replace("http://localhost:8080/service.html");
        var city = "";
        if ($(".city_text").find("option:selected").val()=="贵阳"){
            city="guiyang";
        }
        if ($(".city_text").find("option:selected").val()=="安顺"){
            city="anshun";
        }
        if ($(".city_text").find("option:selected").val()=="六盘水"){
            city="liupanshui";
        }
        if ($(".city_text").find("option:selected").val()=="遵义"){
            city="zunyi";
        }
        if ($(".city_text").find("option:selected").val()=="铜仁"){
            city="tongren";
        }
        if ($(".city_text").find("option:selected").val()=="毕节"){
            city="bijie";
        }
        if ($(".city_text").find("option:selected").val()=="黔南"){
            city="qiannan";
        }
        if ($(".city_text").find("option:selected").val()=="黔东南"){
            city="qiandongnan";
        }
        if ($(".city_text").find("option:selected").val()=="黔西南"){
            city="kaili";
        }
        weather_1(city);
        zhishu(city);
    },1000);
    weather_map();
    warn();


    function weather_1(city) {
        // 基于准备好的dom，初始化echarts实例
        var myLineChart = echarts.init(document.getElementById("temperature"));
        var da = [
            {
                name: '高温',
                type: 'line',
                data: [31, 31, 32, 32, 33, 33, 33, 30, 31, 30, 27, 27, 28, 31],
                markLine: {
                    data: [
                        {type: 'average', name: '平均气温'}
                    ],
                    itemStyle: {
                        normal: {
                            color: '#66ffcc'
                        }
                    },
                    symbol: 'none', //去掉箭头
                }
            },
            {
                name: '低温',
                type: 'line',
                data: [25, 25, 25, 27, 27, 27, 28, 28, 28, 27, 25, 25, 26, 26.5],
                color: '#9AFE2E',
                markPoint: {
                    data: [
                        // { name: '周最低', value: -2, xAxis: 1, yAxis: -1.5 }
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均气温'},
                    ],
                    itemStyle: {
                        normal: {
                            color: '#66ffcc'
                        }
                    },
                    symbol: 'none', //去掉箭头
                }
            }];
        myLineChart.setOption({
            tooltip: {
                trigger: 'axis',
                // axisPointer: {
                //     type: 'cross',
                //     label: {
                //       backgroundColor: '#6a7985'
                //     }
                //   }
            },
            legend: {
                data: ['最高温度', '最低温度'],
                textStyle: {
                    color: '#fff'
                }
            },
            dataset: {
                dimensions: ['dateId', '最高温度', '最低温度'],
                source: []
            },
            grid: {
                right: 50,
                left: 20,

            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
                axisLine: {
                    // onZero: false,
                    show: false,
                    lineStyle: {
                        color: 'rgb(7,40,81)',
                    }
                },
                axisTick: {
                    show: false,
                },

            },
            yAxis: {
                type: 'value',
                axisLine: {
                    show: false,

                },
                axisTick: {       //y轴刻度线
                    show: false,
                },
                splitLine: {
                    show: false, //去掉Y轴分割线
                },
                axisLabel: {
                    show: false, //去掉刻度值
                },
                min: 15,
                // max:32,
            },
            series: da
        });
        var xdata = [];//x轴
        var ydata1 = [];//x轴
        var ydata2 = [];//x轴
        var url = 'http://localhost:8080/data/getWeather?city='+city;
        $.getJSON(url, function (data) {
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                // console.log(arr[i].week);
                xdata.push(arr[i].week);
                ydata1.push(arr[i].max_temp);
                ydata2.push(arr[i].min_temp);

                $('.date-day1').html(arr[0].month_day);
                $('.date-day2').html(arr[1].month_day);
                $('.date-day3').html(arr[2].month_day);
                $('.date-day4').html(arr[3].month_day);
                $('.date-day5').html(arr[4].month_day);
                $('.date-day6').html(arr[5].month_day);
                $('.date-day7').html(arr[6].month_day);

                $('.date-week1').html(arr[0].week);
                $('.date-week2').html(arr[1].week);
                $('.date-week3').html(arr[2].week);
                $('.date-week4').html(arr[3].week);
                $('.date-week5').html(arr[4].week);
                $('.date-week6').html(arr[5].week);
                $('.date-week7').html(arr[6].week);

                $('.date-weather1').html(arr[0].weather);
                $('.date-weather2').html(arr[1].weather);
                $('.date-weather3').html(arr[2].weather);
                $('.date-weather4').html(arr[3].weather);
                $('.date-weather5').html(arr[4].weather);
                $('.date-weather6').html(arr[5].weather);
                $('.date-weather7').html(arr[6].weather);

                $('.date-wind1').html(arr[0].wind);
                $('.date-wind2').html(arr[1].wind);
                $('.date-wind3').html(arr[2].wind);
                $('.date-wind4').html(arr[3].wind);
                $('.date-wind5').html(arr[4].wind);
                $('.date-wind6').html(arr[5].wind);
                $('.date-wind7').html(arr[6].wind);

                var weather = String(arr[i].weather);
                // var img = document.createElement("img");
                // img.setAttribute("class", "pngtqico");
                // $(".date-img1").append(img);

                if (weather ==="多云") {
                    document.getElementsByClassName("pngtqico").src="../weather/b6.png";
                    // $(".pngtqico").attr("src","../weather/b6.png");
                    // $('.pngtqico').setAttribute("src", "../weather/b6.png")
                }if (weather ==="晴") {
                    document.getElementsByClassName("pngtqico").src="../weather/b0.png";
                    // $('.pngtqico').setAttribute("src", "../weather/b0.png")
                }if (weather ==="雨") {
                    // $('.pngtqico').setAttribute("src", "../weather/b7.png")
                }if (weather ==="阴") {
                    // $('.pngtqico').setAttribute("src", "../weather/b2.png")
                }if (weather ==="多云转晴") {
                    // $('.pngtqico').setAttribute("src", "../weather/b5.png")
                }if (weather ==="中雨") {
                    // $('.pngtqico').setAttribute("src", "../weather/b8.png")
                }if (weather ==="晴转多云") {
                    // $('.pngtqico').setAttribute("src", "../weather/b1.png")
                }if (weather ==="雨转晴") {
                    // $('.pngtqico').setAttribute("src", "../weather/b3.png")
                }if (weather ==="阴转雨") {
                    // $('.pngtqico').setAttribute("src", "../weather/b7.png")
                }if (weather ==="小雨") {
                    // $('.pngtqico').setAttribute("src", "../weather/b7.png")
                }if (weather ==="小雨转晴") {
                    // $('.pngtqico').setAttribute("src", "../weather/b3.png")
                }if (weather ==="多云转阴") {
                    // $('.pngtqico').setAttribute("src", "../weather/b2.png")
                }if (weather ==="雨转多云") {
                    // $('.pngtqico').setAttribute("src", "../weather/b2.png")
                }if (weather ==="中雨转小雨") {
                    // $('.pngtqico').setAttribute("src", "../weather/b8.png")
                }if (weather ==="小雨转多云") {
                    // $('.pngtqico').setAttribute("src", "../weather/b2.png")
                }

            }
            myLineChart.setOption({
                xAxis: {
                    data: xdata
                },
                series: [
                    {
                        name: "高温",
                        data: da[0].data = ydata1
                    },
                    {
                        name: "低温",
                        data: da[1].data = ydata2
                    }]
            })
        });
    }

    function weather_map() {
        $.getJSON("http://localhost:8080/data/getWarnCount", function (data) {
            var now = data.data;
            for (var i = 0; i < now.length; i++) {
                var li = document.createElement("li");
                var span1  = document.createElement("span");
                var span2  = document.createElement("span");
                span1.setAttribute("class","cityName1 cnt");
                span2.setAttribute("class","totalCount1 cnt");
                span1.textContent= now[i].name;
                span2.textContent  = now[i].value;
                li.appendChild(span1);
                li.appendChild(span2);
                $("#comment3>ul").append(li);
            }
            // var lineInfo = document.getElementById('comment3');
        });
        var dataList = [
            // { name: '贵阳市', value: 59 },
            // { name: '六盘水市', value: 12 },
            // { name: '毕节市', value: 42 },
            // { name: '遵义市', value: 12 },
            // { name: '安顺市', value: 34 },
            // { name: '黔西南布依族苗族自治州', value: 15 },
            // { name: '黔南布依族苗族自治州', value: 16 },
            // { name: '黔东南苗族侗族自治州', value: 18 },
            // { name: '铜仁市', value: 8 },
        ];
        var myChart = echarts.init(document.getElementById('gzmap'));
        option = {
            tooltip: {
                formatter: function (params, ticket, callback) {
                    return params.seriesName + '<br />' + params.name + '：' + params.value + '处'
                }//数据格式化
            },
            // visualMap: {
            //     // type:'piecewise',
            //     orient: 'horizontal',
            //     min: 0,
            //     max: 50, //最大值
            //     right: '22%',
            //     itemHeight: '200',
            //     textStyle: {
            //         color: "#fff"
            //     },
            //     text: ['多', '少'],
            //     inRange: {
            //         color: ['#2A2AF8', '#1D4A8E', '#050584','#050584','#040463']
            //     },
            //     show: true,

            // },
            series: [
                {
                    name: '预警数',
                    type: 'map',
                    map: '贵州',
                    geoIndex: 0,
                    data: dataList,
                    roam: true,//开启缩放和平移
                    zoom: 0.8,//视角缩放比例
                    y: -30,
                    label: {
                        normal: {
                            show: true,  //省份名称
                            color: '#fff',

                        },
                        emphasis: {
                            show: true,
                        }
                    },
                    itemStyle: {
                        normal: {
                            // areaColor: '#424447',
                            areaColor: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: 'rgb(41,61,77)'},
                                    {offset: 0.5, color: 'rgb(54,63,76)'},
                                    {offset: 1, color: '#424447'}
                                ]
                            ),
                            borderColor: 'rgb(6,221,254)',
                            borderWidth: 1.2,
                        },
                        emphasis: {
                            areaColor: '#1E90FF',
                            color: '#fff'
                        }
                    }
                }
            ]
        };
        $.getJSON('http://localhost:8080/data/getWarnCount', function (data) {
            //alert(data);
            var name = [];
            var arr = data.data;
            // console.log(data.data);
            for (var i = 0; i < arr.length; i++) {
                if (arr[i].name == "黔东南市") arr[i].name = "黔东南苗族侗族自治州";
                if (arr[i].name == "黔南市") arr[i].name = "黔南布依族苗族自治州";
                if (arr[i].name == "凯里市") arr[i].name = "黔西南布依族苗族自治州";
            }

            myChart.setOption({
                series: {
                    data: arr
                }
            })
        });
        myChart.setOption(option);
        window.addEventListener("resize", function () {
            myChart.resize();
        });

        // myChart.on('click', function (params) {
        //     alert(params.name);
        // });
    }

    function warn() {
        var url = 'http://localhost:8080/data/getWarnByCity';
        // alert(url);
        // console.log(url);
        $.getJSON(url, function (data) {
            var arr = data.data;
            $("#comment1").html("");
            for (var i = 0; i < arr.length; i++) {
                var li1 = document.createElement("li");
                var li2 = document.createElement("li");
                var li3 = document.createElement("li");
                li1.setAttribute("class", "c-2");
                li2.setAttribute("class", "c-2");
                li3.setAttribute("class", "tar");
                li1.textContent = arr[i].city_name;
                li2.textContent = arr[i].date_str;
                li3.textContent = arr[i].message;
                $("#comment1").append(li1);
                $("#comment1").append(li2);
                $("#comment1").append(li3);
            }
        });


    }

    function zhishu(city) {
        var url = 'http://localhost:8080/data/getTextByCity?city='+city;
        $.getJSON(url, function (data) {
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                if (arr[i].index_type === "感冒指数") {
                    $('.jianyi1').html(arr[i].text);
                }
                if (arr[i].index_type === "过敏指数") {
                    $('.jianyi2').html(arr[i].text);
                }
                if (arr[i].index_type === "穿衣指数") {
                    $('.jianyi3').html(arr[i].text);
                }
                if (arr[i].index_type === "运动指数") {
                    $('.jianyi4').html(arr[i].text);
                }
                if (arr[i].index_type === "洗车指数") {
                    $('.jianyi5').html(arr[i].text);
                }
                if (arr[i].index_type === "紫外线指数") {
                    $('.jianyi6').html(arr[i].text);
                }
            }
            // $(".zi").html("");
            // for (var i = 0; i < arr.length; i++) {
            //     var li = document.createElement("li");
            //     var div1 = document.createElement("div");
            //     var img1 = document.createElement("img");
            //     var span1 = document.createElement("span");
            //     var div2 = document.createElement("div");
            //     li.setAttribute("class","zhishu");
            //     div1.setAttribute("class","zi");
            //     if (arr[i].index_type === "感冒指数"){
            //         img1.setAttribute("src",'../images/ganmao.jpg');
            //         continue;
            //     }if (arr[i].index_type === "过敏指数"){
            //         img1.setAttribute("src",'../images/jiaotong.jpg');
            //         continue;
            //     }if (arr[i].index_type === "穿衣指数"){
            //         img1.setAttribute("src",'../images/yifu.jpg');
            //         continue;
            //     }if (arr[i].index_type === "运动指数"){
            //         img1.setAttribute("src",'../images/yundong.jpg');
            //         continue;
            //     }if (arr[i].index_type === "洗车指数"){
            //         img1.setAttribute("src",'../images/fangshai.jpg');
            //         continue;
            //     }if (arr[i].index_type ==="紫外线指数"){
            //         img1.setAttribute("src",'../images/shufu.jpg');
            //         continue;
            //     }
            //     span1.setAttribute("class","zs");
            //     div2.setAttribute("class","jianyi");
            //     span1.textContent = arr[i].index_type;
            //     div2.textContent = arr[i].text;
            //     $(".life>ul>li>.zi").append(img1);
            //     $(".life>ul>li>.zi").append(span1);
            //     $(".life>ul>li").append(div1);
            //     $(".life>ul>li").append(div2);
            //     $(".life>ul").append(li);
            // }
        });
    }
});


















