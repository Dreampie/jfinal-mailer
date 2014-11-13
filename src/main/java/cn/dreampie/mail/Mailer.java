package cn.dreampie.mail;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mailer.sendHtml("测试","173956022@qq.com","<a href='www.dreampie.cn'>Dreampie</a>");
 * Created by wangrenhui on 14-5-6.
 */
public class Mailer {
  private static Logger logger = LoggerFactory.getLogger(Mailer.class);

  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
  public static void sendText(String subject, String body, String... recipients) throws EmailException {
    SimpleEmail simpleEmail = new SimpleEmail();
    configEmail(subject, simpleEmail, recipients);
    simpleEmail.setMsg(body);
    simpleEmail.send();
    logger.info("send email to {}", StringUtils.join(recipients));
  }


  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
  public static void sendHtml(String subject, String body, String... recipients) throws EmailException {
    sendHtml(subject, body, null, recipients);
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param attachment 附件
   * @param recipients 收件人
   */
  public static void sendHtml(String subject, String body, EmailAttachment attachment, String... recipients) throws EmailException {
    HtmlEmail htmlEmail = new HtmlEmail();
    configEmail(subject, htmlEmail, recipients);
    htmlEmail.setHtmlMsg(body);
    // set the alternative message
    htmlEmail.setTextMsg("Your email client does not support HTML messages");
    if (attachment != null)
      htmlEmail.attach(attachment);
    htmlEmail.send();
    logger.info("send email to {}", StringUtils.join(recipients));
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param attachment 附件
   * @param recipients 收件人
   */
  public static void sendAttachment(String subject, String body, EmailAttachment attachment, String... recipients) throws EmailException {

    MultiPartEmail multiPartEmail = new MultiPartEmail();
    configEmail(subject, multiPartEmail, recipients);
    multiPartEmail.setMsg(body);
    // add the attachment
    if (attachment != null)
      multiPartEmail.attach(attachment);
    multiPartEmail.send();
    logger.info("send email to {}", StringUtils.join(recipients));
  }

  public static void configEmail(String subject, Email email, String... recipients) throws EmailException {
    MailerConf mailerConf = MailerPlugin.mailerConf;
    email.setCharset(mailerConf.getCharset());
    email.setSocketTimeout(mailerConf.getTimeout());
    email.setSocketConnectionTimeout(mailerConf.getConnectout());
    email.setCharset(mailerConf.getEncode());
    email.setHostName(mailerConf.getHost());
    if (!mailerConf.getSslport().isEmpty())
      email.setSslSmtpPort(mailerConf.getSslport());
    if (!mailerConf.getPort().isEmpty())
      email.setSmtpPort(Integer.parseInt(mailerConf.getPort()));
    email.setSSLOnConnect(mailerConf.isSsl());
    email.setStartTLSEnabled(mailerConf.isTls());
    email.setDebug(mailerConf.isDebug());
    email.setAuthentication(mailerConf.getUser(), mailerConf.getPassword());
    email.setFrom(mailerConf.getFrom(), mailerConf.getName());
    email.setSubject(subject);
    email.addTo(recipients);
  }

}
