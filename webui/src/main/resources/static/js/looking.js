$(function () {
    var city = "guiyang";
    $(".chose_enter").click(function () {
        var city = "";
        if ($(".city_text").find("option:selected").val()=="贵阳"){
            city="guiyang";
        }if ($(".city_text").find("option:selected").val()=="安顺"){
            city="anshun";
        }if ($(".city_text").find("option:selected").val()=="铜仁"){
            city="tongren";
        }if ($(".city_text").find("option:selected").val()=="黔西南"){
            city="kaili";
        }if ($(".city_text").find("option:selected").val()=="六盘水"){
            city="liupanshui";
        }if ($(".city_text").find("option:selected").val()=="毕节"){
            city="bijie";
        }if ($(".city_text").find("option:selected").val()=="黔南"){
            city="qiannan";
        }if ($(".city_text").find("option:selected").val()=="黔东南"){
            city="qiandongnan";
        }
        v1(city);
        v2(city);
        v3(city);
        v4(city);
        v5(city);
        v6(city);
        v7(city);
        v7(city);
    });
    v1(city);
    v2(city);//未来7天气候分布饼图
    v3(city);
    v4(city);
    v5(city);
    v6(city);
    v7(city);
    v8(city);

    function v1(city) {
        var url = 'http://localhost:8080/data/getTempAndHumidityAndAirByCity_day?city='+city;
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('v1'));

        myChart.setOption({
            title: {
                text: '一天空气质量变化曲线图',
                textStyle: {
                    color: '#fff',
                    fontSize: 14,
                },
                x: 'left',
                y: 'top',
            },
            tooltip: {},
            legend: {
                data: ['空气质量'],
                textStyle: {
                    color: '#fff'
                },
                x: '380',
                icon: 'bar',
            },
            grid: {
                left: '5%',
                right: '8%',
                bottom: '8%',
                containLabel: true
            },
            // dataset: {
            //     dimensions: [],
            //     source: []
            // },
            xAxis: {
                name: '时间/h',
                nameLocation: 'center',
                nameGap: 25,
                data: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
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
            yAxis: [{
                type: 'value',
                name: '空气质量指数AQI',
                nameLocation: 'center',
                nameGap: 30,
                axisLine: {
                    show: true,
                    color: '#fff',
                },
                axisLabel: {      // 坐标轴的标签
                    show: true,    // 是否显示
                    color: '#fff',  // 默认轴线的颜色
                    interval: 0,
                },
                axisLine: {
                    lineStyle: {
                        color: '#e0e0e0'
                    }
                },
                splitLine: {    // gird 区域中的分割线
                    show: true,   // 是否显示
                    lineStyle: {
                        color: '#fff',
                    }
                },
                interval: 5, //值之间的间隔
            },
                {
                    type: 'value',
                    min: 0,
                    interval: 5,
                    axisTick: {show: false},
                    axisLine: {
                        lineStyle: {
                            color: '#e0e0e0'
                        }
                    },
                    axisLabel: {
                        textStyle: {
                            // 在这里设置右边Y轴文字颜色
                            color: 'transparent'
                        }
                    }
                }
            ],
            series: [{
                name: '空气质量',
                type: 'bar',
                stack: "one",
                itemStyle: {
                    normal: {
                        color: '#58ACFA'
                    }
                },
                markLine: {
                    data: [
                        {
                            type: 'average',
                            name: '平均值',
                            color: "#fff"
                        },

                    ],
                    itemStyle: {
                        normal: {
                            color: '#FE2E9A'
                        }
                    },
                    symbol: 'none', //去掉箭头
                    label: {
                        formatter: ''
                    }
                },
                barGap: '5%',
                data: [10, 30, 33, 20, 20, 30, 9, 30, 20, 14, 21, 13, 15, 26, 17, 30, 21, 20, 12, 36, 35, 20, 27, 30],

            }
            ]
        });
        var xdata2 = [];//x轴
        var ydata2 = [];//y轴
        $.getJSON(url, function (data) {
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                xdata2.push(arr[i].hour);
                console.log("=====" + arr[i].airquality);
                ydata2.push(arr[i].airquality)
            }
            myChart.setOption({
                xAxis: {
                    data: xdata2
                },
                series: {
                    data: ydata2
                }
            })
        });
    }

    function v2(city) {
        var url = 'http://localhost:8080/data/getWeatherByCity7_14?city='+city+'&num=7'
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('v2'));

        option = {
            title: {
                text: '未来7天气候分布饼图',
                textStyle: {
                    color: '#fff',
                    fontSize: 14,
                },
                x: 'left',
                y: 'top',
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)',
                // position: ['80%', '50%']
                // axisPointer: { type: 'shadow' }
            },
            // itemStyle: {
            //     borderColor: '#fff',
            //     borderWidth: 1
            // },
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
            series: [
                {
                    name: '气候',
                    type: 'pie',
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        avoidLabelOverlap: false,
                    },
                    labelLine: {
                        show: false
                    },
                    label: {
                        show: false,
                    },
                    data: [          // 数据数组，name 为数据项名称，value 为数据项值
                        {value: 71, name: '中雨转多云'},
                        {value: 71, name: '阵雨转中雨'},
                        {value: 142, name: '中雨'},
                        {value: 570, name: '雨'},
                        {value: 71, name: '小雨'},
                        {value: 71, name: '多云'},
                    ],
                    color: ['yellow', '#BDBDBD', 'skyblue', '#EE2C2C', '#01A9DB', 'pink', '#FAAC58', '#2EFE2E', '#848484', '#FA5882']
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

        $.getJSON(url, function (data) {
            // console.log(data);
            myChart.setOption({
                series: [{
                    data: data.data
                }]
            })
        })
    }

    function v3(city) {
        var url = 'http://localhost:8080/data/getWeatherByCity7_14?city='+city+'&num=14';
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('v3'));

        option = {
            title: {
                text: '未来14天气候分布饼图',
                textStyle: {
                    color: '#fff',
                    fontSize: 14,
                },
                x: 'left',
                y: 'top',
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)',
                // position: ['-50%', '50%']
                // axisPointer: { type: 'shadow' }
            },
            // itemStyle: {
            //     borderColor: '#fff',
            //     borderWidth: 1
            // },
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
                    color: ['yellow', '#BDBDBD', 'skyblue', 'pink', '#01A9DB', '#EE2C2C', '#FAAC58', '#2EFE2E', '#848484', '#FA5882']
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);

        $.getJSON(url, function (data) {
            // console.log(data);
            myChart.setOption({
                series: [{
                    data: data.data
                }]
            })
        })
    }

    function v4(city) {
        var url = 'http://localhost:8080/data/getTempAndHumidityAndAirByCity_day?city='+city;
        // 基于准备好的dom，初始化echarts实例
        var myLineChart = echarts.init(document.getElementById("v4"));
        myLineChart.setOption({
            title: {
                text: '一天温度变化曲线图',
                textStyle: {
                    color: '#fff',
                    fontSize: 14
                },
                x: 'left',
                y: 'top'
            },
            tooltip: {
                trigger: 'item',
            },
            legend: {
                data: ['温度'],
                textStyle: {
                    color: '#fff'
                },
                x: '400',
                icon: 'rectangle',
            },
            grid: {
                top: 70,
                bottom: 50
            },
            xAxis: {
                name: '时间/h',
                type: 'category',
                nameLocation: 'center',
                nameGap: 25,
                data: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                axisTick: {
                    show: true,
                    alignWithLabel: true
                },
                axisLabel: {
                    interval: 0, //代表显示所有x轴标签显示
                    margin: 5, // 刻度标签与轴线之间的距离
                }
            },
            yAxis: {
                type: 'value',
                name: '摄氏度/°C',
                axisLine: {
                    lineStyle: {
                        color: '#fff'
                    },
                    show: false,
                },
                axisLabel: {
                    formatter: '{value} °C',
                },
                scale: true
            },
            series:
                {
                    name: '温度',
                    type: 'line',
                    data: [30, 30, 31, 28, 26, 26, 29, 31, 28, 28, 29, 30, 28, 26, 27, 30, 21, 31, 31, 31, 31, 20, 27, 30],
                    markPoint: {
                        data: [
                            {type: 'max', name: '最大值'},
                            {type: 'min', name: '最小值'}
                        ]
                    },
                    //     areaStyle: {
                    //         origin: 'start',
                    //         color:'#A4A4A4',
                    //    },
                    markLine: {
                        data: [
                            {type: 'average', name: '平均温度'}
                        ],
                        itemStyle: {
                            normal: {
                                color: '#58ACFA'
                            }
                        },
                        symbol: 'none', //去掉箭头


                    },
                    // smooth: 0.2,
                    symbol: 'circle',     //折点设定为实心点
                    symbolSize: 10,   //设定实心点的大小
                    itemStyle: {
                        normal: {
                            color: '#5bc0de'
                        }
                    }
                },

        });

        var xdata2 = [];//x轴
        var ydata2 = [];//x轴
        $.getJSON(url, function (data) {
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                console.log(arr[i].hour);
                console.log(arr[i].temperature);
                xdata2.push(arr[i].hour);
                ydata2.push(arr[i].temperature)
            }
            myLineChart.setOption({
                xAxis: {
                    data: xdata2
                },
                series: {
                    data: ydata2
                }
            })
        });
    }

    function v5(city) {
        var url = 'http://localhost:8080/data/getWindLevelByCity?city='+city;
        // 基于准备好的dom，初始化echarts实例
        var myLineChart = echarts.init(document.getElementById("v5"));
        myLineChart.setOption({
            title: {
                text: '一天风级图',
                textStyle: {
                    color: '#fff',
                    fontSize: 14,
                },
                x: 'left',
                y: '10',
            },
            angleAxis: {
                type: 'category',
                data: ['90°', '45°', '0°', '315°', '270°', '225°', '180°', '135°'],
                zlevel: 4,
                boundaryGap: true, //标签和数据点都会在两个刻度之间的带(band)中间
                axisTick: {
                    show: false, //是否显示坐标轴刻度
                    alignWithLabel: false,

                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#fff'
                    },
                },
                axisLabel: {
                    color: '#fff',
                },


            },

            radiusAxis: {
                axisLabel: {
                    // margin: -65,
                    textStyle: {
                        fontSize: 10,
                        color: '#fff',
                    },
                    // position: 'top',
                },
                zlevel: 3,
                polarIndex: 0,
                show: true,
                // scale: true,
            },
            polar: {
                color: '#fff',
                // center: ['50%', '50%'],
            },
            series: [{
                type: 'bar',
                data: [1.0, 1.5, 1.7, 2.4, 1.6, 2.1, 1, 1.4],
                coordinateSystem: 'polar',
                // name: 'A',
                stack: 'a',
                // emphasis: {
                //     focus: 'series'
                // },
                // barGap: '-100%',
                zlevel: 2,
                itemStyle: {
                    color: ['SteelBlue'],
                },
                barWidth: 60,
            },
            ],
        });
        var ydata = []
        $.getJSON(url, function (data) {
            console.log(data.data.level);
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                ydata.push(arr[i].level)
            }
            myLineChart.setOption({
                series: [{
                    data: ydata
                }]
            })
        })

    }

    function v6(city) {
        var url = 'http://localhost:8080/data/getWindLevelByCity14?city='+city;
        // 基于准备好的dom，初始化echarts实例
        var myLineChart = echarts.init(document.getElementById("v6"));
        myLineChart.setOption({
            title: {
                text: '未来14天风级图',
                textStyle: {
                    color: '#fff',
                    fontSize: 14,
                },
                x: 'left',
                y: '10',
            },
            angleAxis: {
                type: 'category',
                data: ['90°', '45°', '0°', '315°', '270°', '225°', '180°', '135°'],
                zlevel: 4,
                boundaryGap: true, //标签和数据点都会在两个刻度之间的带(band)中间
                axisTick: {
                    show: true, //是否显示坐标轴刻度
                    alignWithLabel: false,

                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#fff'
                    },
                    interval: "auto",
                },
                axisLabel: {
                    color: '#fff',
                },
                polarIndex: 0,                       //角度轴所在的极坐标系的索引，默认使用第一个极坐标系
                startAngle: 90,                      //起始刻度的角度，默认为 90 度，即圆心的正上方。0 度为圆心的正右方。
                // clockwise: true,

            },

            radiusAxis: {
                axisLabel: {
                    // margin: -65,
                    textStyle: {
                        fontSize: 10,
                        color: '#fff',
                    },
                    // position: 'top',
                },
                zlevel: 3,
                polarIndex: 0,
                show: true,
                // scale: true,
            },

            polar: {
                color: '#fff',
                // center: ['50%', '50%'],
            },
            series: [{
                type: 'bar',
                data: [0.5, 0.6, 8.5, 4, 3.8, 3.4,8.5],
                coordinateSystem: 'polar',
                // name: 'A',
                stack: 'a',
                // emphasis: {
                //     focus: 'series'
                // },
                // barGap: '-100%',
                zlevel: 2,
                itemStyle: {
                    color: ['SteelBlue'],
                },
                barWidth: 60,
            },
            ],
        });
        var ydata = [];
        $.getJSON(url, function (data) {
            console.log(data.data.level);
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                ydata.push(arr[i].level)
            }
            myLineChart.setOption({
                series: [{
                    data: ydata
                }]
            })
        })

    }

    function v7(city) {
        var url = 'http://localhost:8080/data/getTempAndHumidityAndAirByCity_day?city='+city;

        var myLineChart = echarts.init(document.getElementById("v7"));
        myLineChart.setOption({
            title: {
                text: '一天相对湿度变化曲线图',
                textStyle: {
                    color: '#fff',
                    fontSize: 14
                },
                x: 'left',
                y: 'top'
            },
            tooltip: {
                trigger: 'item',
            },
            grid: {
                top: 70,
                bottom: 50
            },
            legend: {
                data: ['相对湿度'],
                textStyle: {
                    color: '#fff'
                },
                x: '380',
                icon: 'rectangle',

            },
            xAxis: {
                name: '时间/h',
                type: 'category',
                nameLocation: 'center',
                nameGap: 25,
                data: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                axisTick: {
                    show: true,
                    alignWithLabel: true
                },
                axisLabel: {
                    interval: 0, //代表显示所有x轴标签显示
                    margin: 5, // 刻度标签与轴线之间的距离
                }
            },
            yAxis: {
                type: 'value',
                name: '百分比/%',
                axisLine: {
                    lineStyle: {
                        color: '#fff'
                    },
                    show: false,
                },
                axisLabel: {
                    formatter: '{value} %',
                },
                min: 0,
                max: 100,

            },
            series:
                {
                    name: '相对湿度',
                    type: 'line',
                    data: [40, 50, 60, 40, 40, 50, 69, 60, 70, 74, 61, 53, 65, 66, 77, 60, 61, 50, 62, 76, 66, 70, 56, 60],
                    markPoint: {
                        data: [
                            {type: 'max', name: '最大值'},
                            {type: 'min', name: '最小值'}
                        ]
                    },
                    areaStyle: {
                        opacity: 0.6,
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#013ADF'
                        }, {
                            offset: 1,
                            color: '#191970'
                        }])
                    },
                    markLine: {
                        data: [
                            {
                                type: 'average',
                                name: '平均值'
                            }
                        ],
                        itemStyle: {
                            normal: {
                                color: 'red'
                            }
                        },
                        // silent: true, // 去除平均线的样式
                        symbol: 'none', //去掉箭头
                    },
                    symbol: 'circle',     //折点设定为实心点
                    symbolSize: 10,   //设定实心点的大小
                    itemStyle: {
                        normal: {
                            color: '#013ADF', // 折点颜色
                            lineStyle: {
                                color: 'skyblue' //折线颜色
                            }
                        },

                    }
                },

        });
        var xdata2 = [];//x轴
        var ydata2 = [];//x轴
        $.getJSON(url, function (data) {
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                xdata2.push(arr[i].hour);
                ydata2.push(arr[i].humidity)
            }
            myLineChart.setOption({
                xAxis: {
                    data: xdata2
                },
                series: {
                    data: ydata2
                }
            })
        });
    }

    function v8(city) {
        var url = 'http://localhost:8080/data/getTempMaxAndMinByCity14?city='+city;
        // 基于准备好的dom，初始化echarts实例
        var myLineChart = echarts.init(document.getElementById("v8"));
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
            }]
        myLineChart.setOption({
            title: {
                text: '未来14天高低温温变化曲线图',
                textStyle: {
                    color: '#fff',
                    fontSize: 14
                },
                x: 'left'
            },
            tooltip: {
                trigger: 'axis',
                // axisPointer: {
                //     type: 'cross'
                // }
            },
            legend: {
                data: ['高温', '低温'],
                textStyle: {
                    color: '#fff'
                },
                x: '360',
                icon: 'line',
            },
            dataset: {
                dimensions: ['dateId', '高温', '低温'],
                source: []
            },
            grid: {
                top: 70,
                bottom: 50
            },
            xAxis: {
                name: '未来天数/天',
                nameLocation: 'center',
                nameGap: 25,
                type: 'category',
                // boundaryGap: false,
                data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14],
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                axisTick: {
                    show: false,
                    alignWithLabel: true
                },
            },
            yAxis: {
                name: '摄氏度/°C',
                type: 'value',
                axisLine: {
                    lineStyle: {
                        color: '#fff'
                    },
                    show: false,
                },
                axisLabel: {
                    formatter: '{value} °C'
                },
                scale: true,
            },
            series: da
        });

        var xdata = [];//x轴
        var ydata1 = [];//x轴
        var ydata2 = [];//x轴
        $.getJSON(url, function (data) {
            var arr = data.data;
            for (var i = 0; i < arr.length; i++) {
                xdata.push(arr[i].date_14);
                ydata1.push(arr[i].max_temp);
                ydata2.push(arr[i].min_temp);
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

});


















