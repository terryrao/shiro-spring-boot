package com.terryrao.shiro.config;

import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * 图形验证码相配置
 */
@Configuration
@ConditionalOnClass(ImageCaptchaService.class)
@ConditionalOnProperty(prefix = "tx.captcha",name = "random-word",matchIfMissing = true)
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaConfig {


    private final CaptchaProperties properties;
    public CaptchaConfig(CaptchaProperties properties) {
        this.properties = properties;
    }

    /**
     * 图形验证码
     */
    @Bean("captchaService")
    public ImageCaptchaService genericManageableCaptchaService() {
        //随机字样
        RandomWordGenerator wordGenerator = new RandomWordGenerator(properties.getRandomWord());
        //字体
        RandomFontGenerator fontGenerator = new RandomFontGenerator(16, 18, new Font[]{new Font("Arial", Font.PLAIN, 18)});
        //背景
        UniColorBackgroundGenerator backGenUni = new UniColorBackgroundGenerator(95, 35);
        //颜色
        Color colorGreen = new Color(properties.getR(), properties.getG(), properties.getB()); //绿

//        Color colorGrey = new Color(200,255,200); //灰
        SingleColorGenerator colorGen = new SingleColorGenerator(colorGreen);
        //文本混淆
        DecoratedRandomTextPaster decoratedPaster = new DecoratedRandomTextPaster(properties.getMinAcceptedWordLength()
                , properties.getMaxAcceptedWordLength(), colorGen, null);

        ComposedWordToImage wordtoimage = new ComposedWordToImage(fontGenerator, backGenUni, decoratedPaster);

        GimpyFactory captchaFactory = new GimpyFactory(wordGenerator, wordtoimage);

        GenericCaptchaEngine imageEngine = new GenericCaptchaEngine(new GimpyFactory[]{captchaFactory});
        //图形验证码
        return new GenericManageableCaptchaService(imageEngine, 180, 10000, 10000);
    }

}
