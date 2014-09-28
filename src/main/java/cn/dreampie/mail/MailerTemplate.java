package cn.dreampie.mail;

import com.jfinal.kit.PathKit;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;

/**
 * Created by wangrenhui on 2014/7/2.
 */
public class MailerTemplate {

  private static MailerTemplate mailerTemplate = new MailerTemplate();
  /**
   * 邮件模板的存放位置
   */
  private static final String TEMPLATE_PATH = "template/";
  /**
   * 模板引擎配置
   */
  private static Configuration configuration;
  /**
   * 参数
   */
  private static Map<Object, Object> parameters;
  /**
   * 模板加载位置
   */
  private static List<TemplateLoader> loaders = new ArrayList<TemplateLoader>();

  static {
    configuration = new Configuration();
//        ClassTemplateLoader ctl= new ClassTemplateLoader(MailerTemplate.class, TEMPLATE_PATH);
    try {
      Enumeration<URL> resources = MailerTemplate.class.getClassLoader().getResources(TEMPLATE_PATH);
      URL resource = null;
      while (resources.hasMoreElements()) {
        resource = resources.nextElement();
        loaders.add(new FileTemplateLoader(new File(resource.getFile())));
      }
      File webDir = new File(PathKit.getWebRootPath() + File.separator + TEMPLATE_PATH);
      if (webDir.exists())
        loaders.add(new FileTemplateLoader(webDir));
      TemplateLoader[] templateLoaders = new TemplateLoader[loaders.size()];
      configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(templateLoaders)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    configuration.setEncoding(Locale.getDefault(), "UTF-8");
    configuration.setDateFormat("yyyy-MM-dd HH:mm:ss");
  }

  public static MailerTemplate me() {
    //初始化参数
    parameters = new HashMap<Object, Object>();
    return mailerTemplate;
  }

  public MailerTemplate set(String attr, Object value) {
    parameters.put(attr, value);
    return this;
  }

  public String getText(String templateFile) {
    try {
      Template template = configuration.getTemplate(templateFile);
      StringWriter stringWriter = new StringWriter();
      template.process(parameters, stringWriter);
      return stringWriter.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
