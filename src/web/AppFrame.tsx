import { Outlet } from "react-router-dom";
import { Toolbar, Typography, AppBar } from "@mui/material";

export const AppFrame = () => {
    return (
        <>
            <AppBar position="sticky">
                <Toolbar>
                    <img className="app-logo" src="/img/app.png" />
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1, textAlign:"center" }}>
                        模拟器
                    </Typography>
                </Toolbar>
            </AppBar>
            <Outlet />
        </>
    );
}