package com.it.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DataMapper {

    @Select("select `month`,temp_avg from avg_city where city = #{city} and year=#{year} ORDER BY `month`")
    List<Map<String,Object>> getAvgByCity(String city,String year);

    @Select("select weather as `name`,count(weather) as `value` FROM ((select `weather` FROM weather14 WHERE city = #{city} LIMIT #{num}) as temp) GROUP BY weather")
    List<Map<String,Object>> getWeatherByCity7_14(String city,int num);

    @Select("select `level` from weather14 where city=#{city}")
    List<Map<String,Object>> getWindLevelByCity14(String city);

    @Select("select `level` from weather1 where city=#{city}")
    List<Map<String,Object>> getWindLevelByCity(String city);

    @Select("select `hour`,temperature,humidity,airquality from weather1 where city=#{city}")
    List<Map<String,Object>> getTempAndHumidityAndAirByCity_day(String city);

    @Select("select date_14, max_temp,min_temp from weather14 where city=#{city}")
    List<Map<String,Object>> getTempMaxAndMinByCity14(String city);

    @Select("select date_str, message,city_name from warn")
    List<Map<String,Object>> getWarnByCity();

    @Select("SELECT city_name as `name`,count(city) as `value` from warn GROUP BY city_name")
    List<Map<String,Object>> getWarnCount();

    @Select("select date_14, weather,min_temp,max_temp,wind_1 from weather14 where city=#{city} limit 7")
    List<Map<String,Object>> getWeather_7(String city);

    @Select("select frequency, index_type,text from text_fre where city=#{city} limit 6")
    List<Map<String,Object>> getTextByCity(String city);

    @Select("SELECT `month`,temp_max,temp_min from weather_city where city=#{city} and `year`=#{year} ORDER BY `month`")
    List<Map<String,Object>> getMaxAndMinTem(String city,String year);

    @Select("SELECT temp_avg as value from avg_city where city=#{city} and `year`=#{year} and `month`=#{month}")
    List<Map<String,Object>> getMonthTemp(String city,String year,String month);




}
