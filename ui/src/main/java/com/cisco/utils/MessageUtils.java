package com.cisco.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.zul.Messagebox;

/**
 * Created by Alf on 03.09.2014.
 */
public class MessageUtils {

	public static void showErrorMessage(Exception e) {
		Throwable rootCause = ExceptionUtils.getRootCause(e);
		if(rootCause != null) {
			Messagebox.show(rootCause.getMessage(), null, 0, Messagebox.ERROR);
		} else {
			Messagebox.show(e.getMessage(), null, 0, Messagebox.ERROR);
		}
	}
}
