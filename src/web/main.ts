import React from 'react';
import ReactDOM from 'react-dom/client';
import { App } from "./App";
import "./main.scss";

class Main{
    private _reactRoot;

    public constructor(tag: string){
        const mountPoint = document.getElementById(tag);
        this._reactRoot = ReactDOM.createRoot(mountPoint!);
    }

    public Run = async()=>{
        const app = App();
        this._reactRoot.render(app);
        return `cuckoo-web(reactjs-${React.version})`;
    }
}

export const objMain = new Main("app");
objMain.Run().then(console.info);
