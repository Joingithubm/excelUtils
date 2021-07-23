package com.utils;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.utils.bo.DataSourceBO;
import com.utils.config.CustomCellWriteHandler;
import com.utils.entity.DataSource;
import com.utils.entity.TableName;
import com.utils.enums.DataSourceEnum;
import com.utils.util.DataSourceUtils;
import com.utils.util.StyleUtils;
import com.utils.vo.DataSourceVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;

/**
 * @Description:
 * @Author Zengfc
 * @Date 2021/7/9 9:39
 * @Version 1.0
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class Application {


    private final static String NAME_SPACE = "com.utils.mapper.CommonMapper.";

    private static Map<String,String> local = new HashMap<>();
    private static Integer cnt = 0;
    private static String markString = "__over_";



    public static void main(String[] args) throws Exception {

        if (args.length != 6){
            System.out.println("参数有误");
            return;
        }
        DataSourceVo dataSourceVo = createDataSourceVoByArgs(args);

            exportExcel(dataSourceVo, args[5]);


        SpringApplication.run(Application.class,args);
    }

    public static void exportExcel( DataSourceVo dataSourceVo, String filePath) throws Exception {

        OutputStream out = null;
        ExcelWriter excelWriter = null;

        try {

             out = new FileOutputStream(filePath);

            List<DataSource> dataSources = new ArrayList<>();

            DataSourceBO sourceBO = convertDataSourceVOToBO(dataSourceVo);

            javax.sql.DataSource dataSource = DataSourceUtils.druidDataSource(sourceBO);
            SqlSessionTemplate sqlSessionTemplate = DataSourceUtils.mainSqlSessionTemplate(dataSource);
            List<Map<String, Object>> mapList = sqlSessionTemplate.selectList(NAME_SPACE + "selectDataSource");

            convertMapToDataSource(mapList, dataSources);

            // 标题样式
            WriteCellStyle headWriteCellStyle = StyleUtils.getHeadStyle();

            // 内容样式
            WriteCellStyle contentWriteCellStyle = StyleUtils.getContentStyle();

            // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

             excelWriter = EasyExcel.write(out)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .registerWriteHandler(new CustomCellWriteHandler()).build();

            WriteSheet writeSheet = EasyExcel.writerSheet(0, "数据库说明").head(DataSource.class).build();
            excelWriter.write(dataSources, writeSheet);

            StringBuilder builder = new StringBuilder();
            //循环每张表
            for (int i = 0; i < dataSources.size(); i++) {

                List<Map<String, Object>> mapList1 = sqlSessionTemplate.selectList(NAME_SPACE + "selectTableToMap", "'" + dataSources.get(i).getTableName() + "'");
                List<TableName> iptv_stb_5M = new ArrayList<>();
                convertMapToTableName(mapList1, iptv_stb_5M);
                builder.append(dataSources.get(i).getTableName());
                if (local.containsKey(builder.toString()) || local.containsKey(builder.toString().toUpperCase())) {
                    Integer tem = 10000+i;
                    builder.append("_"+tem);
                }
                System.out.println("local:"+local+"  sheetName:"+builder.toString());
                if (builder.toString().length()>= 31){
                    builder.delete(20,builder.capacity());
                    builder.append(markString);
                    builder.append(cnt);
                    cnt++;
                }
                WriteSheet writeSheet1 = EasyExcel.writerSheet(i + 1, builder.toString()).head(TableName.class).build();
                excelWriter.write(iptv_stb_5M, writeSheet1);
                local.put(builder.toString(), null);
                builder.delete(0,builder.capacity());
            }

            //千万别忘记finish 会帮忙关闭流
/*            excelWriter.finish();
            out.close();*/
        }
         catch (FileNotFoundException e) {
             System.out.println("找不到文件或文件路径错误, 文件："+ filePath);
            }finally {
                try {
                    if(excelWriter != null){
                        excelWriter.finish();
                    }

                    if(out != null){
                        out.close();
                    }
                } catch (IOException e) {
                    System.out.println("excel文件导出失败, 失败原因：{}"+ e.getStackTrace());
                }
            }
    }

    private static DataSourceVo createDataSourceVoByArgs(String[] args){

        DataSourceVo vo = new DataSourceVo();
        vo.setHost(args[0]);
        vo.setPort(args[1]);
        vo.setServiceName(args[2]);
        vo.setUsername(args[3]);
        vo.setPassword(args[4]);

        return vo;
    }

    private static DataSourceBO convertDataSourceVOToBO(DataSourceVo dataSourceVo){
        DataSourceBO bo = new DataSourceBO();
        String url = "jdbc:oracle:thin:@"+dataSourceVo.getHost()+":"+dataSourceVo.getPort()+"/"+dataSourceVo.getServiceName();
        bo.setUrl(url);
        bo.setUsername(dataSourceVo.getUsername());
        bo.setPassword(dataSourceVo.getPassword());
        bo.setDriverClassName(DataSourceEnum.ORACLE.getDriver());
        return bo;
    }


    private static void convertMapToTableName(List<Map<String,Object>> map, List<TableName> entities){

        map.forEach(u->{
            TableName tableName = new TableName();
            tableName.setTableName(u.get("TABLE_NAME").toString());
            tableName.setColumnName(u.get("COLUMN_NAME").toString());
            tableName.setDataType(u.get("DATA_TYPE").toString());
            tableName.setDataLength(u.get("DATA_LENGTH").toString());
            tableName.setNullAble(u.get("NULLABLE").toString());
            tableName.setComments(u.get("COMMENTS") == null ? null:u.get("COMMENTS").toString());
            entities.add(tableName);
        });
    }


    private static void convertMapToDataSource(List<Map<String,Object>> map, List<DataSource> entities){
        map.forEach(u->{
            DataSource dataSource = new DataSource();
            dataSource.setTableName(u.get("TABLE_NAME") == null ? null : u.get("TABLE_NAME").toString());
            dataSource.setComments(u.get("COMMENTS") == null ? null : u.get("COMMENTS").toString());
            entities.add(dataSource);
        });
    }


}
