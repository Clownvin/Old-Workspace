package com.eimacs.lab06;

import com.eimacs.digest.UserInfoDialog;

/**
 *
 * @author IMACS Curriculum Development Group
 * @version 1.0 April 25, 2014
 */
public class Lab06Runner
{
    public static int checkIndex = 0;
    
    public static void main( String[] args )
    {
        /* 
         * When instructed to do so, uncomment the following three
         * statements and comment out the rest of the main method
         * before running the project.
        */
//        UserInfoDialog dlg = new UserInfoDialog( null, true );
//        dlg.setLocationRelativeTo( null );
//        dlg.setVisible( true );

        Picture p = new Picture( "beach.jpg" );
        p.explore();
    }
    
}
