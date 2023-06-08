import React from "react";
import { Switch, FormControlLabel } from "@mui/material";
import "./home.scss";


export const Home = () => {
    return (
        <div className="home-container">
            <div className="target-block">
                <span className="title">Kafka</span>
            </div>
            <div className="target-block">
                <span  className="title">MQTT</span>
                <FormControlLabel control={<Switch />} label="ON/OFF" />
            </div>
            <div className="target-block">
                <span  className="title">TCP</span>
            </div>
        </div>
    );
}