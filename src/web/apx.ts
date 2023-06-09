import axios from "axios";

class APX{
    public fetchVersion = async ()=>{
        const resp = await axios.get("/api/version");
        return resp.data;
    }

    public fetchState = async ()=>{
        const resp = await axios.get("/api/apow/state");
        return resp.data;
    }

    public start = async ()=>{
        const resp = await axios.put("/api/apow/start");
        return resp.data;
    }

    public stop = async ()=>{
        const resp = await axios.put("/api/apow/stop");
        return resp.data;
    }
}

export const apx = new APX();