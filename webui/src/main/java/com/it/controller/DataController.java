package com.it.controller;

import com.it.bean.Result;
import com.it.mapper.DataMapper;
import com.it.phoenix.PhoneixJdbcService;
import com.it.time.TimeTranstationUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-08-31 15:15
 */
@RestController
@RequestMapping("data")
public class DataController {
    @Autowired
    private DataMapper dataMapper;

    /**
     * @Description //TODO 历史气象信息
     * @Date 12:46 2021/9/7
     * @Param [city, year, month]
     * @return com.it.bean.Result
     **/
    @RequestMapping("getWeatherAll")
    public Result getAll(@Param("city") String city,@Param("year") String year,@Param("month") String month) throws Exception {
        if (Integer.parseInt(month)<10) month="0"+month;
        System.out.println(city);
        List<Map<String, Object>> weather = PhoneixJdbcService.getAll(city, year, month);
        for (Map<String,Object> map: weather) {
            System.out.println(map.toString());
        }
        return Result.success(weather);
    }
    /**
     * @Description //TODO 历史温度
     * @Date 12:46 2021/9/7
     * @Param [city, year, month]
     * @return com.it.bean.Result
     **/
    @RequestMapping("getTemperature")
    public Result getTemperature(@Param("city") String city,@Param("year") String year,@Param("month") String month) throws Exception {
        if (Integer.parseInt(month)<10) month="0"+month;
        System.out.println(city);
        List<Map<String, Object>> weather = PhoneixJdbcService.getTemperature(city, year, month);
        for (Map<String,Object> map: weather) {
            System.out.println(map.toString());
        }
        return Result.success(weather);
    }
    @RequestMapping("getMeteo")
    public Result getMeteo(@Param("city") String city,@Param("year") String year,@Param("month") String month) throws Exception {
        if (Integer.parseInt(month)<10) month="0"+month;
        System.out.println(month);
        System.out.println(city);
        List<Map<String, Object>> weather = PhoneixJdbcService.getMeteo(city, year, month);
        for (Map<String,Object> map: weather) {
            System.out.println(map.toString());
        }
        return Result.success(weather);
    }
    /**
     * @Description //TODO 历史平均温度
     * @Date 12:46 2021/9/7
     * @Param [city, year]
     * @return com.it.bean.Result
     **/
    @RequestMapping("getAvgByCity")
    public Result getAvgByCity(@Param("city") String city,@Param("year") String year) throws Exception {
        if (city==""){
            city = "guiyang";
            if (year==""){
                year = "2021";
            }
        }
        System.out.println(city);
        List<Map<String, Object>> weather = dataMapper.getAvgByCity(city,year);
        return Result.success(weather);
    }
    /**
     * @Description //TODO 7,14天气饼图接口
     * @Date 12:45 2021/9/7
     * @Param [city, num]
     * @return com.it.bean.Result
     **/
    @RequestMapping("getWeatherByCity7_14")
    public Result getWeatherByCity7_14(@Param("city") String city,@Param("num") String num) throws Exception {
        if (city==""){
            city = "guiyang";
        }
        List<Map<String, Object>> weather = dataMapper.getWeatherByCity7_14(city,Integer.parseInt(num));
        return Result.success(weather);
    }
    @RequestMapping("getWindLevelByCity14")
    public Result getWindLevelByCity14(@Param("city") String city) throws Exception {
        if (city==""){
            city = "guiyang";
        }
        List<Map<String, Object>> weather = dataMapper.getWindLevelByCity14(city);
        return Result.success(weather);
    }

    @RequestMapping("getWindLevelByCity")
    public Result getWindLevelByCity(@Param("city") String city) throws Exception {
        if (city==""){
            city = "guiyang";
        }
        List<Map<String, Object>> weather = dataMapper.getWindLevelByCity(city);
        return Result.success(weather);
    }
    @RequestMapping("getTempAndHumidityAndAirByCity_day")
    public Result getTempAndHumidityAndAirByCity_day(@Param("city") String city) throws Exception {
        if (city==""){
            city = "guiyang";
        }
        List<Map<String, Object>> weather = dataMapper.getTempAndHumidityAndAirByCity_day(city);
        return Result.success(weather);
    }
    @RequestMapping("getTempMaxAndMinByCity14")
    public Result getTempMaxAndMinByCity14(@Param("city") String city) throws Exception {
        if (city==""){
            city = "guiyang";
        }
        List<Map<String, Object>> weather = dataMapper.getTempMaxAndMinByCity14(city);
        return Result.success(weather);
    }

    @RequestMapping("getWarnByCity")
    public Result getWarnByCity() throws Exception {
        List<Map<String, Object>> weather = dataMapper.getWarnByCity();
        return Result.success(weather);
    }

    @RequestMapping("getWarnCount")
    public Result getWarnCount() throws Exception {
        List<Map<String, Object>> weather = dataMapper.getWarnCount();
        return Result.success(weather);
    }

    @RequestMapping("getWeather_7")
    public Result getWeather_7(@Param("city") String city) throws Exception {
        if (city==""){
            city = "guiyang";
        }
        List<Map<String, Object>> weather = dataMapper.getWeather_7(city);
        return Result.success(weather);
    }

    @RequestMapping("getTextByCity")
    public Result getTextByCity(@Param("city") String city) throws Exception {
        if (city==""){
            city = "guiyang";
        }
        List<Map<String, Object>> weather = dataMapper.getTextByCity(city);
        return Result.success(weather);
    }
    @RequestMapping("getMaxAndMinTem")
    public Result getMaxAndMinTem(@Param("city") String city,@Param("year") String year) throws Exception {
        List<Map<String, Object>> weather = dataMapper.getMaxAndMinTem(city,year);
        return Result.success(weather);
    }
    @RequestMapping("getMonthTemp")
    public Result getMonthTemp(@Param("city") String city,@Param("year") String year,@Param("month") String month) throws Exception {
        if (Integer.parseInt(month)<10) month="0"+month;
        List<Map<String, Object>> weather = dataMapper.getMonthTemp(city,year,month);
        return Result.success(weather);
    }

    @RequestMapping("getWeather")
    public Result getWeather(@Param("city") String city) throws Exception {
        if (city==""){
            city = "guiyang";
        }
        List<Map<String, Object>> weather = dataMapper.getWeather_7(city);
        for (Map<String, Object> stringObjectMap : weather) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            String year_month = format.format(System.currentTimeMillis());
            String day = (String) stringObjectMap.get("date_14");
            String final_date = year_month+"-"+day;
            String[] split = final_date.split("-");
            String month_day = split[1]+"月"+split[2]+"日";
            String week = TimeTranstationUtils.getWeek(final_date);
            String wind = (String)stringObjectMap.get("wind_1");
            if (wind.equals("无持续风向")){
                wind="无风向";
            }
            stringObjectMap.remove("wind_1");
            stringObjectMap.remove("date_14");
            stringObjectMap.put("month_day",month_day);
            stringObjectMap.put("wind",wind);
            stringObjectMap.put("week",week);
        }
        return Result.success(weather);
    }
}
