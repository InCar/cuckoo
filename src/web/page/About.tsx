import React, { useState } from "react";
import { Button, TextField } from "@mui/material";
import { apx } from "~/apx";
import { RVM } from "~/rvm";
import "./about.scss";

export const About = () => {
    const dataRaw = "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";
    const [rawContent, setRawContent] = useState(dataRaw);
    const [hexContent, setHexContent] = useState("");

    const onSubmit = async () => {
        const data = await apx.decodeIm2t(rawContent);
        const rvm = new RVM();
        const listEvents = rvm.split(data.hex);

        // insert a new line every 80 characters
        // const hex = data.hex.replace(/(.{80})/g, "$1\n");
        // setHexContent(hex);

        const hex = listEvents.concat().join("$\n");
        setHexContent(hex);
    };

    const onClear = () => {
        setHexContent("");
    };

    const onContentChanged = (e:any) => {
        setRawContent(e.target.value);
    };

    return (
        <div className="about-container">
            <div className="raw-content">
                <TextField multiline fullWidth minRows={5} maxRows={10} label="file content" value={rawContent} onChange={onContentChanged}></TextField>
            </div>
            <div className="actions">
                <Button variant="contained" onClick={onSubmit}>解码</Button>
                <Button variant="contained" onClick={onClear}>清除</Button>
            </div>
            <div className="hex-content">
                <TextField multiline fullWidth minRows={5} label="HEX" value={hexContent}></TextField>
            </div>
        </div>
    );
}