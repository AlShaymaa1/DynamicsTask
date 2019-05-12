package com.dynamicsapp.usecaseLayer.domain;

public class SavedInstance {


        private static SavedInstance sSoleInstance;
        private boolean isChecked;

        private SavedInstance(){

        }

        public static SavedInstance getInstance(){
            if (sSoleInstance == null){
                sSoleInstance = new SavedInstance();
            }

            return sSoleInstance;
        }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}


