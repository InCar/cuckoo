import * as React from "react";
import styles from "./ScriptEditor.module.scss";

interface ScriptEditorProps{
    value: string;
}

export const ScriptEditor = (props: ScriptEditorProps) => {
    
    const makeContent = (value:string)=>{
        var listLines = value.split("\n");
        return listLines.map((line, index)=>{
            return (<div key={index}>
                <span>{line}</span>
            </div>)
        });
    };

    return (<div className={styles.script_container} contentEditable="true" suppressContentEditableWarning={true}>
        {makeContent(props.value)}
    </div>)
};