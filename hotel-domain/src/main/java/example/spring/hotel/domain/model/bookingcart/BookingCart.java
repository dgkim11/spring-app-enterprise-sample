package example.spring.hotel.domain.model.bookingcart;

import example.spring.hotel.domain.model.bookingcart.exception.AddToCartException;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookingCart {
    private Long customerId;
    private List<BookingCartItem> bookingCartItems = new LinkedList<>();

    public BookingCart(Long customerId) {
        this.customerId = customerId;
    }

    public BookingCart(Long customerId, List<BookingCartItem> items)  {
        this.customerId = customerId;
        this.bookingCartItems = items;
    }

    public void addBookingCartItem(BookingCartItem item) throws AddToCartException {
        validateBookingCartItem(item);
        item.setCustomerId(customerId);
        bookingCartItems.add(item);
    }

    private void validateBookingCartItem(BookingCartItem cartItem) throws AddToCartException {
        if(cartItem.getBookingDateTime() == null) throw new AddToCartException("bookingDate가 존재하지 않습니다.");
        if(hasSameProductAndBookingDateInCart(cartItem))
            throw new AddToCartException("동일한 상품이 동일한 예약 날짜로 이미 장바구니에 있습니다.");
        if(! cartItem.getProduct().isProductSellable())
            throw new AddToCartException("해당 상품은 판매중인 상품이 아닙니다.");
    }

    /**
     * 장바구니에 추가할 상품이 이미 동일한 예약 날짜로 장바구니에 존재하는지 확인.
     */
    private boolean hasSameProductAndBookingDateInCart(BookingCartItem cartItem)    {
        for(BookingCartItem item : bookingCartItems)   {
            if(item.getBookingDateTime().equals(cartItem.getBookingDateTime())) return true;
        }
        return false;
    }
}
