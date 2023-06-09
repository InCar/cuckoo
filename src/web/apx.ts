import axios from "axios";

class APX{
    public fetchVersion = async ()=>{
        const resp = await axios.get("/api/version");
        return resp.data;
    }
}

export const apx = new APX();