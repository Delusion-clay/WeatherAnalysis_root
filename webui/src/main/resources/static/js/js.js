$(function () {

    var year = "2021";
    var month = "1";
    var city = "guiyang";
    $(".chose_enter").click(function () {
        $("#comment2").html("");
        var monthText = $(".month_text").find("option:selected").val();
        var month = monthText.substring(0, monthText.length - 1);
        var year = $(".year_text").find("option:selected").val();
        var city = "";
        if ($(".city_text").find("option:selected").val() == "贵阳") {
            city = "guiyang";
        }
        if ($(".city_text").find("option:selected").val() == "安顺") {
            city = "anshun";
        }
        if ($(".city_text").find("option:selected").val() == "铜仁") {
            city = "tongren";
        }
        if ($(".city_text").find("option:selected").val() == "黔西南") {
            city = "qianxinan";
        }
        if ($(".city_text").find("option:selected").val() == "六盘水") {
            city = "liupanshui";
        }
        if ($(".city_text").find("option:selected").val() == "毕节") {
            city = "bijie";
        }
        if ($(".city_text").find("option:selected").val() == "黔南") {
            city = "qiannan";
        }
        if ($(".city_text").find("option:selected").val() == "黔东南") {
            city = "qiandongnan";
        }
        echarts_1(year, month, city);
        echarts_3(year, month, city);
        echarts_4(year, city);
        lunbo(year, month, city);
        echarts_5(year, city);
        echarts_6(year, month, city);

    });
    echarts_1(year, month, city);
    echarts_3(year, month, city);
    echarts_4(year, city);
    lunbo(year, month, city);
    echarts_5(year, city);
    echarts_6(year, month, city);

    function echarts_1(year, month, city) {
        /*--------------------月攻击统计量-----------------------------*/
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart1'));

        myChart.setOption({
            tooltip: {},
            legend: {
                textStyle: {
                    color: '#fff'
                }
            },
            toolbox: {
                show: true,
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    dataView: {readOnly: false},
                    magicType: {type: ['line', 'bar']},
                    restore: {},
                    saveAsImage: {}
                },
                iconStyle: {
                    normal: {
                        color: '#FFF',//设置颜色
                    }
                }
            },
            grid: {
                left: '3%',
                right: '10%',
                bottom: '8%',
                containLabel: true
            },
            xAxis: {
                data: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24],
                type: 'category',
                axisLine: {
                    lineStyle: {
                        color: '#fff',
                    }
                },
                axisTick: {
                    show: false,
                    alignWithLabel: true
                },
                axisLabel: {
                    interval: 0, //代表显示所有x轴标签显示
                    margin: 5, // 刻度标签与轴线之间的距离
                }
            },
            yAxis: {
                type: 'value',
                axisLine: {
                    show: true,
                },
                axisLabel: {      // 坐标轴的标签
                    show: true,    // 是否显示
                    color: '#999',  // 默认轴线的颜色
                    interval: 0,
                    formatter: '{value} °C'
                },
                splitLine: {    // gird 区域中的分割线
                    show: true,   // 是否显示
                    lineStyle: {
                        color: '#E5E5E5',
                        width: 0.7,
                        type: 'dashed'   // dashed
                    }
                },
                interval: 5, //值之间的间隔
            },
            series: [{
                type: 'bar',
                stack: "one",
                itemStyle: {
                    normal: {
                        color: '#58ACFA'
                    }
                },
                axisLabel: {      // 坐标轴的标签
                    formatter: '{value} °C'
                },
                barGap: '5%',
                data: [20, 21, 24, 25, 26, 24, 26, 25, 20, 24, 22, 24,
                    {
                        value: 22,
                        itemStyle: {
                            color: '#FE2E2E',
                        }
                    }, 20, 27, 23, 22, 25, 21, 24, 25, 26, 27, 23, 20],
            }
            ]
        });
        var xdata = [];//x轴
        var ydata = [];//y轴
        // http://localhost:8080/data/getTemperature?city=anshun&year=2021&month=7
        var url = "http://localhost:8080/data/getTemperature?city=" + city + "&year=" + year + "&month=" + month;
        $.getJSON(url, function (data) {
            var arr = data.data;
            xdata = [];
            ydata = [];
            for (var i = 0; i < arr.length; i++) {
                var day = arr[i].rowkey.substring(8, 10);
                if (day.charAt(0) == 0) day = day.substring(1, 2);
                xdata.push(day);
                ydata.push(arr[i].temperature.split("/")[0].split("℃")[0]);
            }
            myChart.setOption({
                series: {
                    data: ydata,
                },
                xAxis: {
                    data: xdata
                }
            })
        })
    }

    function echarts_3(year, month, city) {
        /*--------------------贵州温差对比-----------------------------*/
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById("echart3"));
        option = {

            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)',
                // position: ['-50%', '50%']
                // axisPointer: { type: 'shadow' }
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                right: 10,
                top: 10,
                // bottom: 10,
                textStyle: {
                    color: '#fff'
                },
            },
            grid: {
                // left: '3%',
                right: '10%',
                bottom: '8%',
                containLabel: true
            },
            series: [
                {
                    name: '气候',
                    type: 'pie',
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    data: [          // 数据数组，name 为数据项名称，value 为数据项值
                        {value: 71, name: '中雨转阵雨'},
                        {value: 71, name: '阵雨转中雨'},
                        {value: 71, name: '阵雨'},
                        {value: 71, name: '阴转雨'},
                        {value: 71, name: '雨转阴'},
                        {value: 357, name: '雨'},
                        {value: 71, name: '阵雨转雨'},
                        {value: 71, name: '多云转阵雨'},
                        {value: 71, name: '多云'},
                        {value: 71, name: '阵雨转多云'},
                    ],
                    labelLine: {
                        show: false
                    },
                    label: {
                        show: false,
                    },
                    color: ['rgb(192,192,29)', '#BDBDBD', '#C27191', 'pink', '#77BBBB',
                        'rgb(206,15,130)', '#FAAC58', '#4ED369', '#848484', '#FA5882', '#55DDDD', '#638ED0']
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        $.getJSON('http://localhost:8080/data/getMeteo?city=' + city + '&year=' + year + '&month=' + month, function (data) {
            // console.log(data);
            myChart.setOption({
                series: [{
                    data: data.data
                }]
            })
        })
    }

    function echarts_4(year, city) {
        /*--------------------贵州月份平均气温-----------------------------*/
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart4'));

        myChart.setOption({
            tooltip: {},
            legend: {
                textStyle: {
                    color: '#fff'
                }
            },
            toolbox: {
                show: true,
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    dataView: {readOnly: false},
                    magicType: {type: ['line', 'bar']},
                    restore: {},
                    saveAsImage: {}
                },
                iconStyle: {
                    normal: {
                        color: '#FFF',//设置颜色
                    }
                }
            },
            grid: {
                top: 70,
                bottom: 50
            },
            xAxis: {
                type: 'category',
                axisLine: {
                    lineStyle: {
                        color: '#fff',
                    }
                },
                axisTick: {
                    show: false,
                    alignWithLabel: true
                },
                // min: 0,
                // max: 31,
                data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
            },
            yAxis: {
                name: '温度',
                type: 'value',
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#fff',
                    }
                },
                axisLabel: {      // 坐标轴的标签
                    show: true,    // 是否显示
                    color: '#999',  // 默认轴线的颜色
                    // interval: 10,
                },
                splitLine: {    // gird 区域中的分割线
                    show: false,   // 是否显示
                    lineStyle: {
                        color: '#E5E5E5',
                        // width: 0.7,
                    }
                },
                interval: 10, //值之间的间隔
                min: -30,
                max: 30,
            },
            series: [{
                type: 'bar',
                // stack: "one",
                barWidth: '10', //设置柱子宽度
                // showBackground: true,
                itemStyle: {
                    emphasis: {
                        barBorderRadius: 30
                    },
                    normal: {
                        barBorderRadius: [10, 10, 10, 10],
                        label: {
                            show: false,//是否展示
                            textStyle: {
                                fontWeight: 'bolder',
                                fontSize: '12',
                                fontFamily: '微软雅黑',
                            }
                        },
                        color: function (params) {
                            var colorList = [
                                '#00FFFF', '#58FAF4', '#81F7F3', '#A9F5F2', '#CEF6F5', '#FAAC58',
                                '#FE9A2E', '#FF8000', '#FA5858', '#FFBF00', '#FE2E2E', '#FF0040',
                            ];
                            return colorList[params.dataIndex];
                        },
                        shadowBlur: 4,
                        shadowColor: 'rgb(201,52,52)',
                        shadowOffsetX: 0,
                        shadowOffsetY: 2,
                    },


                },
                barGap: '5%',
                data: [-10, -8, -10, 10, 28, 10, 17, 18, 17, 20, 22, 24],
            }]
        });
        var xdata = [];//x轴
        var ydata = [];//y轴
        var url = "http://localhost:8080/data/getAvgByCity?city=" + city + "&year=" + year;
        $.getJSON(url, function (data) {
            var arr = data.data;
            xdata = [];
            ydata = [];
            for (var i = 0; i < arr.length; i++) {
                var month = arr[i].month.substring(1, 2) + "月";
                xdata.push(month);
                ydata.push(arr[i].temp_avg);
            }
            myChart.setOption({
                series: {
                    data: ydata
                },
                xAxis: {
                    data: xdata
                }
            })
        })

    }

    function echarts_5(year, city) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart5'));
        var option;
        var da = [
            {
                name: '最高温',
                type: 'bar',
                itemStyle: {
                    normal: {
                        label: {
                            show: false,//是否展示
                            textStyle: {
                                fontWeight: 'bolder',
                                fontSize: '12',
                                fontFamily: '微软雅黑',
                            }
                        },
                        color: '#CEF6F5',
                        shadowBlur: 4,
                        shadowColor: 'rgb(201,52,52)',
                        shadowOffsetX: 0,
                        shadowOffsetY: 2,
                    },

                },
                data: [2.0, 4.9, 7.0, 23.2, 25.6, 6.7, 15.6, 16.2, 32.6, 20.0, 6.4, 3.3]

            },
            {
                name: '最低温',
                type: 'bar',
                itemStyle: {
                    normal: {
                        label: {
                            show: false,//是否展示
                            textStyle: {
                                fontWeight: 'bolder',
                                fontSize: '12',
                                fontFamily: '微软雅黑',
                            }
                        },
                        color: '#FE2E2E',
                        shadowBlur: 4,
                        shadowColor: 'rgb(201,52,52)',
                        shadowOffsetX: 0,
                        shadowOffsetY: 2,
                    },

                },
                data: [2.6, 5.9, 9.0, 26.4, 28.7, 7.7, 17.6, 18.2, 8.7, 18.8, 6.0, 2.3]
            }
        ];

        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            legend: {
                data: ['最高温', '最低温'],
                textStyle: {
                    color: '#fff'
                },
                x: '240px',
                y: '5px',
            },
            xAxis: [
                {
                    type: 'category',
                    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
                    axisPointer: {
                        type: 'shadow'
                    },
                    axisLine: {
                        onZero: false,
                        lineStyle: {
                            color: '#fff',
                        }
                    },
                    axisLabel: {
                        interval: 0
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    // name: '水量',
                    // axisLabel: {
                    //     formatter: '{value} ml'
                    // }
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#fff',
                        }
                    },
                    axisLabel: {      // 坐标轴的标签
                        show: true,    // 是否显示
                        color: '#999',  // 默认轴线的颜色
                        // interval: 10,
                    },

                    splitLine: {    // gird 区域中的分割线
                        show: true,   // 是否显示
                        lineStyle: {
                            color: '#E5E5E5',
                            width: 0.7,
                            type: 'dashed'   // dashed
                        }
                    },
                    interval: 5, //值之间的间隔
                    min: -10,
                    max: 40,

                },
                // {
                //     type: 'value',
                //     // name: '温度',
                //     axisLine: {
                //         show: false,
                //         lineStyle: {
                //             color: '#fff',
                //         }
                //     },
                //     axisLabel: {      // 坐标轴的标签
                //         show: false,    // 是否显示
                //         color: '#999',  // 默认轴线的颜色
                //         interval: 5,
                //     },
                //     splitLine: {    // gird 区域中的分割线
                //         show: true,   // 是否显示
                //         lineStyle: {
                //             color: '#E5E5E5',
                //             width: 0.7,
                //             type: 'dashed'   // dashed
                //             // width: 0.7,
                //         }
                //     },
                //     interval: 5, //值之间的间隔
                //     min: 0,
                //     max: 50,
                // }
            ],
            series: da
        };
        var xdata = [];
        var ydata1 = [];
        var ydata2 = [];
        $.getJSON('http://localhost:8080/data/getMaxAndMinTem?city='+city+'&year='+year, function (data) {
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                var month = arr[i].month;
                xdata.push(month);
                ydata1.push(arr[i].temp_max);
                ydata2.push(arr[i].temp_min);
            }
            myChart.setOption({
                xAxis: {
                    data: xdata
                },
                series: [
                    {
                        name: "最高温",
                        data: da[0].data = ydata1
                    },
                    {
                        name: "最低温",
                        data: da[1].data = ydata2
                    }]


            })

        });
        option && myChart.setOption(option);

    }

    function echarts_6(year, month, city) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart6'));
        option = {
            series: [{
                type: 'gauge',
                startAngle: 240,
                endAngle: -60,
                min: -10,
                max: 30,
                splitNumber: 5,
                axisLine: {
                    lineStyle: {
                        width: 3,
                        color: [
                            [0, '#fff'],
                            [0.2, '#00ff99'],
                            [0.4, '#00c4ff'],
                            [0.6, '#3c00ff'],
                            [0.8, '#ff00d9'],
                            [1.0, '#FF6E76']
                        ]
                    }
                },
                pointer: {
                    length: '80%',
                    width: 5,
                    offsetCenter: [0, '-60%'],
                    itemStyle: {
                        color: 'auto'
                    }
                },
                axisTick: {
                    length: 12,
                    lineStyle: {
                        color: 'auto',
                        width: 2
                    }
                },
                splitLine: {
                    length: 20,
                    lineStyle: {
                        color: '#fff',
                        width: 3
                    }
                },
                axisLabel: {
                    color: '#fff',
                    fontSize: 20,
                    distance: -50,
                    // formatter: function (value) {
                    //     if (value === 0.6) {
                    //         return '差';
                    //     }
                    //     else if (value === 0.2) {
                    //         return '过';
                    //     }
                    //     else if (value === 0.4) {
                    //         return '中';
                    //     }
                    //     else if (value === 0.6) {
                    //         return '良';
                    //     } else if (value === 1) {
                    //         return '优';
                    //     }
                    // }
                },
                title: {
                    offsetCenter: [0, '-20%'],
                    fontSize: 30
                },
                detail: {
                    fontSize: 50,
                    offsetCenter: [0, '0%'],
                    valueAnimation: true,
                    // formatter: function (value) {
                    //     return Math.round(value * 100);
                    // },
                    color: 'auto'
                },
                data: [{
                    value: 9,
                }]
            }]
        };
        option && myChart.setOption(option);
        $.getJSON('http://localhost:8080/data/getMonthTemp?city=' + city + '&year=' + year + '&month=' + month, function (data) {
            var data1 = data.data;
            console.log(data1);
            myChart.setOption({
                series: [{
                    data: data.data
                }]
            })
        })
    }

    function lunbo(year, month, city) {
        var url = 'data/getWeatherAll?city=' + city + '&year=' + year + '&month=' + month;
        // alert(url);
        console.log(url);
        $.getJSON(url, function (data) {
            var arr = data.data;
            $("#comment1").html("");
            for (var i = 0; i < arr.length; i++) {
                var li = document.createElement("li");
                var li1 = document.createElement("span");
                var li2 = document.createElement("span");
                var li3 = document.createElement("span");
                var li4 = document.createElement("span");
                var li5 = document.createElement("span");
                var li6 = document.createElement("span");
                li1.setAttribute("class", "week");
                li2.setAttribute("class", "date");
                li3.setAttribute("class", "temp");
                li4.setAttribute("class", "meto");
                li5.setAttribute("class", "wind");
                li6.setAttribute("class", "city");
                li1.textContent = arr[i].week;
                var date_row = arr[i].rowkey.substring(6, 10);
                li2.textContent = date_row;
                li3.textContent = arr[i].temperature;
                li4.textContent = arr[i].meteo;
                var windFina = arr[i].wind.split("/")[0];
                li5.textContent = windFina;
                var cityname = arr[i].city;
                if (cityname == "anshun") {
                    li6.textContent = "安顺";
                }
                if (cityname == "guiyang") {
                    li6.textContent = "贵阳";
                }
                if (cityname == "liupanshui") {
                    li6.textContent = "六盘水";
                }
                if (cityname == "zunyi") {
                    li6.textContent = "遵义";
                }
                if (cityname == "bijie") {
                    li6.textContent = "毕节";
                }if (cityname == "tongren") {
                    li6.textContent = "铜仁";
                }if (cityname == "qiannan") {
                    li6.textContent = "黔南";
                }if (cityname == "qiandongnan") {
                    li6.textContent = "黔东南";
                }if (cityname == "qianxinan") {
                    li6.textContent = "黔西南";
                }
                li.appendChild(li1);
                li.appendChild(li2);
                li.appendChild(li3);
                li.appendChild(li4);
                li.appendChild(li5);
                li.appendChild(li6);

                $("#comment1").append(li);
            }
            // $("#week").html(arr.week);
            // $("#date").html(arr.rowkey);
            // $("#temp").html(arr.temperature);
            // $("#weather").html(arr.meteo);
            // $("#wind").html(arr.wind);
            // $("#city").html(arr.city);
        });
    }
});


















