import React, { useState, useMemo, useEffect } from "react";
import styles from "./ScriptEditor.module.scss";

interface ScriptEditorProps{
    value: string;
    txtValue: string[];
}

export const ScriptEditor = (props: ScriptEditorProps) => {
    const [text, setText] = useState(props.value);

    if(props.value !== text){
        setText(props.value);
    }
    props.txtValue[0] = props.value;
    
    const makeContent = (value:string)=>{
        var listLines = value.split("\n");
        return listLines.map((line, index)=>{
            const rgxComment = /\/\/.*$/gi;
            const matchComment = rgxComment.exec(line);
            if(matchComment){
                // remove the comment
                line = line.replace(rgxComment, "");
            }

            const rgx = /^(\+\d+:\d+)(\s+)(\w+)(.*)/gi;
            const match = rgx.exec(line);
            if(match){
                return (<div key={index}>
                    <span className={styles.txt_tm}>{match[1]}</span>
                    <span>{match[2]}</span>
                    <span className={styles.txt_action}>{match[3]}</span>
                    <span>{match[4]}</span>
                    { matchComment && <span className={styles.txt_comment}>{matchComment[0]}</span> }
                </div>)
            }
            else{
                return (<div key={index}>
                    <span>{line}</span>
                    { matchComment && <span className={styles.txt_comment}>{matchComment[0]}</span> }
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
        props.txtValue[0] = unmakeContent(e.target.innerHTML);
    };

    return (<div className={styles.script_container} contentEditable="true" suppressContentEditableWarning={true} onInput={onInput}>
        {content}
    </div>)
};