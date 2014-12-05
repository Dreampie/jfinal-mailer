jfinal-mailer
============

jfinal  mailer  plugin，查看其他插件-> [Maven](http://search.maven.org/#search%7Cga%7C1%7Ccn.dreampie)

maven 引用  ${jfinal-mailer.version}替换为相应的版本如:0.2

```xml
<dependency>
  <groupId>cn.dreampie</groupId>
  <artifactId>jfinal-mailer</artifactId>
  <version>${jfinal-mailer.version}</version>
</dependency>
```

use  easy:


application.properties

```properties

#email smtp.gmail.com smtp.163.com smtp.live.com
smtp.host=smtp.163.com
#smtp.port=465
#smtp.timeout=900000
#smtp.sslport=587
smtp.ssl=false
smtp.tls=false
smtp.debug=true
smtp.user=wangrenhui1990@163.com
smtp.password=xxxx
smtp.name=Dreampie
smtp.from=wangrenhui1990@163.com

```
发送邮件

```java

//emailer插件
plugins.add(new MailerPlugin());

//独立启动
//MailerPlugin mailerPlugin = new MailerPlugin();
//mailerPlugin.start();

//发送带图片的邮件
HtmlEmail htmlEmail = Mailer.getHtmlEmail("测试", "173956022@qq.com");
String cid1 = htmlEmail.embed(new File(PathKit.getWebRootPath() + "/src/main/webapp/image/favicon.ico"), "1");
String cid2 = htmlEmail.embed(new File(PathKit.getWebRootPath() + "/src/main/webapp/image/app/logo.png"), "2");
htmlEmail.setHtmlMsg("<a href='www.dreampie.cn'>Dreampie</a><img src=\"cid:" + cid1 + "\"'/><img src=\"cid:" + cid2 + "\"'/>");

```