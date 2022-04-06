package vip.xiaonuo;

import cn.hutool.log.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 *
 * @author yubaoshan
 * @date 2017/5/21 12:06
 */
@SpringBootApplication
public class LightupApplication {

    private static final Log log = Log.get();

    public static void main(String[] args) {
        SpringApplication.run(LightupApplication.class, args);
        log.info(">>> " + LightupApplication.class.getSimpleName() + " is success!");
    }

}
