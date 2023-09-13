import axios from "axios";

class APX{
    public fetchVersion = async ()=>{
        const resp = await axios.get("/api/version");
        return resp.data;
    }

    public fetchApowState = async ()=>{
        const resp = await axios.get("/api/apow/state");
        return resp.data;
    }

    // speed: 数据包发送数率(包/秒)
    public startApow = async (speed:number)=>{
        const resp = await axios.put("/api/apow/start", {speed});
        return resp.data;
    }

    public stopApow = async ()=>{
        const resp = await axios.put("/api/apow/stop");
        return resp.data;
    }

    public fetchIm2tState = async ()=>{
        const resp = await axios.get("/api/im2t/state");
        return resp.data;
    }

    public startIm2t = async ()=>{
        const resp = await axios.put("/api/im2t/start");
        return resp.data;
    }

    public stopIm2t = async ()=>{
        const resp = await axios.put("/api/im2t/stop");
        return resp.data;
    }

    public fetchScriptText = async()=>{
        const resp = await axios.get("/api/im2t/scripts");
        return resp.data;
    }

    public saveScriptText = async (scripts: string)=>{
        const resp = await axios.put("/api/im2t/scripts", scripts,
            { headers: { 'Content-Type': 'text/plain'} });
        return resp.data;
    }

    public decodeIm2t = async (data: string)=>{
        const resp = await axios.post("/api/im2t/decode", {data});
        return resp.data;
    }
}

export const apx = new APX();