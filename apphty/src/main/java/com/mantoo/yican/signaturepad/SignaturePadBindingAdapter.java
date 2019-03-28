package com.mantoo.yican.signaturepad;



public final class SignaturePadBindingAdapter {

    public static void setOnSignedListener(SignaturePad view, final OnStartSigningListener onStartSigningListener) {
        setOnSignedListener(view, onStartSigningListener, null, null);
    }

    public static void setOnSignedListener(SignaturePad view, final OnSignedListener onSignedListener) {
        setOnSignedListener(view, null, onSignedListener, null);
    }

    public static void setOnSignedListener(SignaturePad view, final OnClearListener onClearListener) {
        setOnSignedListener(view, null, null, onClearListener);
    }

    public static void setOnSignedListener(SignaturePad view, final OnStartSigningListener onStartSigningListener, final OnSignedListener onSignedListener, final OnClearListener onClearListener) {
        view.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                if (onStartSigningListener != null) {
                    onStartSigningListener.onStartSigning();
                }
            }

            @Override
            public void onSigned() {
                if (onSignedListener != null) {
                    onSignedListener.onSigned();
                }
            }

            @Override
            public void onClear() {
                if (onClearListener != null) {
                    onClearListener.onClear();
                }
            }
        });
    }

    public interface OnStartSigningListener {
        void onStartSigning();
    }

    public interface OnSignedListener {
        void onSigned();
    }

    public interface OnClearListener {
        void onClear();
    }

}
