package pers.sun.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import pers.sun.startup.Bootstrap;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * yml配置文件解析
 *
 * @author 曹沫
 * @date 2021/8/30
 */
public final class YamlParser {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    /**
     * 读取配置文件
     */
    public static Map<String, Object> loadConfig(String path) {
        try {
            File file = new File(path);
            InputStream inputStream = null;
            inputStream = new FileInputStream(file);
            Yaml yaml = new Yaml();
            //System.out.println(objectMap.toString());
            //System.out.println(objectMap.get("server").getClass());
            //System.out.println(objectMap.getClass());
            //objectMap.get("server");
            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            logger.error("文件解析错误" + e.getMessage());
            return null;
        }
    }


}
