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

            const state = await apx.fetchState();
            setIsSwitchOn(state.isRunning);
        })();
    }, []);

    const handleSwitchChange = async () => {
        if(isSwitchOn){
            const data = await apx.stop();
            setIsSwitchOn(data.isRunning);
        }else{
            const data = await apx.start();
            setIsSwitchOn(data.isRunning);
        }
    };

    return (
        <div className="home-container">
            <div className="targets">
                <div className="target-block">
                        <span className="title">SAIC-Kafka</span>
                        <div className="content">
                            <FormControlLabel control={<Switch disabled={true} />} label="ON/OFF" />
                        </div>
                </div>
                <div className="target-block">
                    <span  className="title">APOW-MQTT</span>
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
            <div className="version">
                <a href={sourceLink} target="_blank">
                    <svg viewBox="0 0 16 16" width="16" height="16">
                        <path d="M8 0c4.42 0 8 3.58 8 8a8.013 8.013 0 0 1-5.45 7.59c-.4.08-.55-.17-.55-.38 0-.27.01-1.13.01-2.2 0-.75-.25-1.23-.54-1.48 1.78-.2 3.65-.88 3.65-3.95 0-.88-.31-1.59-.82-2.15.08-.2.36-1.02-.08-2.12 0 0-.67-.22-2.2.82-.64-.18-1.32-.27-2-.27-.68 0-1.36.09-2 .27-1.53-1.03-2.2-.82-2.2-.82-.44 1.1-.16 1.92-.08 2.12-.51.56-.82 1.28-.82 2.15 0 3.06 1.86 3.75 3.64 3.95-.23.2-.44.55-.51 1.07-.46.21-1.61.55-2.33-.66-.15-.24-.6-.83-1.23-.82-.67.01-.27.38.01.53.34.19.73.9.82 1.13.16.45.68 1.31 2.69.94 0 .67.01 1.3.01 1.49 0 .21-.15.45-.55.38A7.995 7.995 0 0 1 0 8c0-4.42 3.58-8 8-8Z"></path>
                    </svg>
                </a>
                {version}
            </div>
        </div>
    );
}