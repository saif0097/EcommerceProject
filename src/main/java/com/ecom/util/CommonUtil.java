package com.ecom.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecom.model.ProductOrder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {
	
	@Autowired
	private  JavaMailSender mailSender;

	public  Boolean sendMail(String url,String reciepentEmail ) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("saifmulla2000@gmail.com"," Shopping cart");
		helper.setTo(reciepentEmail);
		
		String content="<p> Hello, </p>" + "<p> You have requested to reset your password.</p>" 
		+ "<p> Click the below to change your password :</p>" + "<p> <a href=\"" + url 
		+ "\"> Change my password </a></p>";
		
		helper.setSubject("Password Reset ");
		helper.setText(content,true);
		mailSender.send(message);
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {
		String siteUrl = request.getRequestURL().toString();
		return  siteUrl.replace( request.getServletPath(), "")  ;
		
	}
	
	String msg="<p>Thank you Order <b>[[orderStatus]].</b> </p>" 
	+ "<p><b>Product Details :</b></p>"
	+ "<p>Name : [[productName]]</p>"
	+ "<p>Category : [[category]] </p>"
	+ "<p>Quantity : [[quantity]] </p>"
	+ "<p>Price : [[price]]</p>"
	+ "<p>Payment Type : [[paymentType]]</p>";

	public Boolean sendMailForProductOrder(ProductOrder order,Integer statusCode) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("saifmulla2000@gmail.com"," Shopping cart");
		helper.setTo(order.getOrderAddress().getEmail());
		
		OrderStatus[] values = OrderStatus.values();
		for(OrderStatus status : values)
		{
			if(status.getId().equals(statusCode))
			{
				msg=msg.replace("[[orderStatus]]", status.getName());
			}
		}
		
		
		msg=msg.replace("[[productName]]", order.getProduct().getTitle());
		msg=msg.replace("[[category]]", order.getProduct().getCategory());
		msg=msg.replace("[[quantity]]", order.getQuantity().toString());
		msg=msg.replace("[[price]]", order.getPrice().toString());
		msg=msg.replace("[[paymentType]]", order.getPaymentType());
		
		
		helper.setSubject("Product Order Status");
		helper.setText(msg,true);
		mailSender.send(message);
		return true;
	}
}
