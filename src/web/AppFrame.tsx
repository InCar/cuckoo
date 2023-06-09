import { Link, Outlet } from "react-router-dom";
import { Toolbar, Typography, AppBar } from "@mui/material";
import { Handyman } from '@mui/icons-material';
import appPng from "./img/app.png";

export const AppFrame = () => {
    return (
        <>
            <AppBar position="sticky">
                <Toolbar>
                    <Link to="/"><img className="app-logo" src={appPng} /></Link>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1, textAlign:"center" }}>
                        模拟器
                    </Typography>
                    <Link to="/About"><Handyman fontSize="large"/></Link>
                </Toolbar>
            </AppBar>
            <Outlet />
        </>
    );
}