import React, { useState, useMemo } from "react";
import styles from "./ScriptEditor.module.scss";

interface ScriptEditorProps{
    value: string;
}

export const ScriptEditor = (props: ScriptEditorProps) => {
    const [isTouched, setIsTouched] = useState(false); // to prevent re-rendering when typing
    const [text, setText] = useState(props.value);

    if(!isTouched && props.value !== text){
        setText(props.value);
    }
    
    const makeContent = (value:string)=>{
        var listLines = value.split("\n");
        return listLines.map((line, index)=>{
            const rgx = /^(\+\d+:\d+)(\s+)(\w+)(.*)/gi;
            const match = rgx.exec(line);
            if(index === 4){
                console.info(line);
            }
            if(match){
                return (<div key={index}>
                    <span className={styles.txt_tm}>{match[1]}</span>
                    <span>{match[2]}</span>
                    <span className={styles.txt_action}>{match[3]}</span>
                    <span>{match[4]}</span>
                </div>)
            }
            else{
                return (<div key={index}>
                    <span>{line}</span>
                </div>)
            }
        });
    };

    const unmakeContent = (content:string)=>{
        // insert \n between divs
        let txt = content.replace(/<\/div>/gi, "</div>\n");
        // remove the ending \n
        txt = txt.replace(/\n$/gi, "");
        
        // replace &nbsp; with space
        txt = txt.replace(/&nbsp;/gi, " ");
        // remove all html tags
        txt = txt.replace(/(<([^>]+)>)/gi, "");

        return txt;
    };

    const content = useMemo(()=>{
        return makeContent(text);
    }, [text]);

    const onInput = (e:any)=>{
        if(!isTouched){
            setIsTouched(true);
        }

        // TODO: delay dirty colorful text
        const txtValue = unmakeContent(e.target.innerHTML);
        if(txtValue !== text){
            // setText(txtValue);
        }
    };

    return (<div className={styles.script_container} contentEditable="true" suppressContentEditableWarning={true} onInput={onInput}>
        {content}
    </div>)
};