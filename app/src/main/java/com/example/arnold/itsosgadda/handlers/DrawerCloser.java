package com.example.arnold.itsosgadda.handlers;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerCloser {
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
