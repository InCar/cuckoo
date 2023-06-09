import React, { useState, useEffect } from "react";
import { Switch, FormControlLabel } from "@mui/material";
import { apx } from "~/apx";
import "./home.scss";

export const Home = () => {
    const [isSwitchOn, setIsSwitchOn] = useState(false);
    const [version, setVersion] = useState("");
    const [sourceLink, setSourceLink] = useState("");

    useEffect(() => {
        (async()=>{
            const data = await apx.fetchVersion();
            setVersion(data.version);
            setSourceLink(data.sourceLink);
        })();
    }, []);

    const handleSwitchChange = () => {
        setIsSwitchOn(!isSwitchOn);
    };

    return (
        <div className="home-container">
            <div className="targets">
                <div className="target-block">
                        <span className="title">Kafka</span>
                        <div className="content">
                            <FormControlLabel control={<Switch disabled={true} />} label="ON/OFF" />
                        </div>
                </div>
                <div className="target-block">
                    <span  className="title">MQTT</span>
                    <div className="content">
                        <FormControlLabel control={<Switch checked={isSwitchOn} onChange={handleSwitchChange} />} label="ON/OFF" />
                    </div>
                </div>
                <div className="target-block">
                    <span  className="title">TCP</span>
                    <div className="content">
                        <FormControlLabel control={<Switch disabled={true} />} label="ON/OFF" />
                    </div>
                </div>
            </div>
            <div className="version"><a href={sourceLink}>{version}</a></div>
        </div>
    );
}