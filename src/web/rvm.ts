
export class RVM{
    public split = (data: string):string[] => {
        let listEvents: string[] = [];

        const buffer = this.hexToBytes(data);
        let pos = 0;

        while(pos < buffer.length){
            // get the first 2 bytes
            const evId = (buffer[pos] << 8) + (buffer[pos + 1]);
            // evID to hex string
            const evIdHex = evId.toString(16).toUpperCase().padStart(4, '0');
            // console.log(`evId: 0x${evIdHex}`);
            
            if(evId <= 0xAFFF){
                // 定长8字节
                const strEvent = data.slice(pos*2, (pos+8)*2);
                listEvents.push(strEvent);
                // move pos to the nex event, 8 bytes ahead
                pos += 8;

            }
            else if(evId <= 0xDFFF){
                // 变长
                // the length is from pos+2 to pos+3
                const length = (buffer[pos + 2] << 8) + (buffer[pos + 3]);
                const strEvent = data.slice(pos*2, (pos+length+4)*2);
                listEvents.push(strEvent);

                // move pos to the next event, length + 4 bytes ahead
                pos += (length + 4);

            }else if(evId == 0xFFFF){
                const strEvent = data.slice(pos*2);
                listEvents.push(strEvent);
                // CRC
                pos += 8;
            }
            else{
                console.error(`Unrecognized evId value: ${evIdHex}`);
                break;
            }
        }
        return listEvents;
    }

    private hexToBytes = (hex: string) => {
        const hexString = hex.replace(/\s/g, ''); // remove any whitespace
        const bytes = new Uint8Array(hexString.length / 2);
        for (let i = 0; i < hexString.length; i += 2) {
            bytes[i / 2] = parseInt(hexString.substring(i, i + 2), 16);
        }
        return bytes;
    }
}