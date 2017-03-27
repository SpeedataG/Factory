package common.utils.security;

import java.lang.reflect.Method;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class CTelephoneInfo {
	private static final String TAG = CTelephoneInfo.class.getSimpleName();
	private String imeiSIM1;// IMEI
	private String imeiSIM2;//IMEI	

	private boolean isSIM1Ready;//sim1
	private boolean isSIM2Ready;//sim2
//	private String iDataConnected1 = "0";//sim1 0 no, 1 connecting, 2 connected, 3 suspended.
//	private String iDataConnected2 = "0";//sim2
	private static CTelephoneInfo CTelephoneInfo;
	private static Context mContext;
	
	private CTelephoneInfo() {
	}

	public synchronized static CTelephoneInfo getInstance(Context context){
	    if(CTelephoneInfo == null) {	
	        CTelephoneInfo = new CTelephoneInfo();
	    }
	    mContext = context;	    
	    return CTelephoneInfo;
	}
	
	public String getImeiSIM1() {
	    return imeiSIM1;
	}
	
	public String getImeiSIM2() {
	    return imeiSIM2;
	}
	
	public boolean isSIM1Ready() {
	    return isSIM1Ready;
	}
	
	public boolean isSIM2Ready() {
	    return isSIM2Ready;
	}
	
	public boolean isDualSim(){
		return imeiSIM2 != null;
	}
	
//	public boolean isDataConnected1(){
//		if(TextUtils.equals(iDataConnected1, "2")||TextUtils.equals(iDataConnected1, "1"))
//			return true;
//		else
//			return false;
//	}
	
//	public boolean isDataConnected2(){
//		if(TextUtils.equals(iDataConnected2, "2")||TextUtils.equals(iDataConnected2, "1"))
//			return true;
//		else
//			return false;
//	}
//

	
	public void setCTelephoneInfo(){
		TelephonyManager telephonyManager = ((TelephonyManager) 
        		mContext.getSystemService(Context.TELEPHONY_SERVICE));	
        CTelephoneInfo.imeiSIM1 = telephonyManager.getDeviceId();;
        CTelephoneInfo.imeiSIM2 = null;	
        try {
            CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceIdGemini", 0);
            CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceIdGemini", 1);

//            CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext, "getDataStateGemini", 0);
//            CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext, "getDataStateGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
            try {
            	 CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceId", 0);
            	 CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceId", 1);

//                 CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext, "getDataState", 0);
//                 CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext, "getDataState", 1);
            } catch (GeminiMethodNotFoundException e1) {
                //Call here for next manufacturer's predicted method name if you wish
                e1.printStackTrace();
            }
        }
        CTelephoneInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
        CTelephoneInfo.isSIM2Ready = false;

        try {
        	CTelephoneInfo.isSIM1Ready = getSIMStateBySlot(mContext, "getSimStateGemini", 0);
        	CTelephoneInfo.isSIM2Ready = getSIMStateBySlot(mContext, "getSimStateGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
            try {
            	CTelephoneInfo.isSIM1Ready = getSIMStateBySlot(mContext, "getSimState", 0);
            	CTelephoneInfo.isSIM2Ready = getSIMStateBySlot(mContext, "getSimState", 1);
            } catch (GeminiMethodNotFoundException e1) {
                //Call here for next manufacturer's predicted method name if you wish
                e1.printStackTrace();
            }
        }
	}
	
	private static  String getOperatorBySlot(Context context, String predictedMethodName, int slotID) 
			 throws GeminiMethodNotFoundException {	
	    String inumeric = null;	
	    TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);	
	    try{	
	        Class<?> telephonyClass = Class.forName(telephony.getClass().getName());	
	        Class<?>[] parameter = new Class[1];
	        parameter[0] = int.class;
	        Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);		        
	        Object[] obParameter = new Object[1];
	        obParameter[0] = slotID;
	        Object ob_phone = getSimID.invoke(telephony, obParameter);	
	        if(ob_phone != null){
	        	inumeric = ob_phone.toString();	
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new GeminiMethodNotFoundException(predictedMethodName);
	    }	
	    return inumeric;
	}
	
	private static  boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
		
	    boolean isReady = false;
	
	    TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	
	    try{
	
	        Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
	
	        Class<?>[] parameter = new Class[1];
	        parameter[0] = int.class;
	        Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);
	
	        Object[] obParameter = new Object[1];
	        obParameter[0] = slotID;
	        Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);
	
	        if(ob_phone != null){
	            int simState = Integer.parseInt(ob_phone.toString());
	            if(simState == TelephonyManager.SIM_STATE_READY){
	                isReady = true;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new GeminiMethodNotFoundException(predictedMethodName);
	    }
	
	    return isReady;
	}
	
	private static class GeminiMethodNotFoundException extends Exception {	

	    /**
		 * 
		 */
		private static final long serialVersionUID = -3241033488141442594L;

		public GeminiMethodNotFoundException(String info) {
	        super(info);
	    }
	}
	
}
