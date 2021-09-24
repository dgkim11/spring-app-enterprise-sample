package example.spring.hotel.infrastructure.domain.mybatis.payment;

import example.spring.hotel.domain.model.payment.Payment;
import example.spring.hotel.domain.model.payment.PaymentInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaymentMapper {
    @Insert("""
            insert into payment (customerId, checkoutId, paidDateTime, totalPrice)
            values (#{customerId}, #{checkoutId}, #{paidDateTime}, #{totalPrice})
             """)
    @Options(useGeneratedKeys = true, keyProperty = "paymentId")
    public void insertPayment(Payment payment);

    @Insert("""
            insert into paymentInfo (paymentId, paymentType, price, accountNumber)
            values (#{paymentId}, #{paymentType}, #{price}, #{accountNumber})
            """)
    void insertPaymentInfo(PaymentInfoRow paymentInfoRow);

    @Select("""
            select * from payment where paymentId = #{paymentId}
            """)
    PaymentRow findPaymentById(Long paymentId);

    @Select("""
            select * from paymentInfo where paymentId = #{paymentId}
            """)
    List<PaymentInfo> findPaymentInfosByPaymentId(Long paymentId);

    @Delete("""
            delete p, i from payment p inner join paymentInfo i on p.paymentId = i.paymentId
            where p.customerId = #{customerId}
            """)
    int deletePaymentByCustomerId(Long customerId);

    @Delete("""
            delete p, i from payment p inner join paymentInfo i on p.paymentId = i.paymentId
            where p.paymentId = #{paymentId}
            """)
    int deletePaymentById(Long paymentId);

}