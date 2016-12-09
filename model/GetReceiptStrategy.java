/**
 * GetReceiptStrategy.java: interface for Strategy pattern on printing receipt
 * Author: Kim Pham
 */

package model;

import java.util.ArrayList;

public interface GetReceiptStrategy {
  String getReceipt(Guest user, ArrayList<Reservation> receipt);
}
