package io.roisagiv.android.redux.examples.shoppingcart;

import android.os.Parcel;
import android.os.Parcelable;

public class Parcelables {

  public static <T extends Parcelable> T deepCopy(T input) {
    Parcel parcel = null;
    try {
      parcel = Parcel.obtain();
      parcel.writeParcelable(input, 0);
      parcel.setDataPosition(0);
      return parcel.readParcelable(input.getClass().getClassLoader());
    } finally {
      if (parcel != null) {
        parcel.recycle();
      }
    }
  }
}
