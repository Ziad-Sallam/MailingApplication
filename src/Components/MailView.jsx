import PropTypes from "prop-types";
import axios from "axios";
import {useParams} from "react-router-dom";
import {CiTrash} from "react-icons/ci";
import {useEffect, useState} from "react";

const mail = {
    sender : PropTypes.string,
    receiver : PropTypes.arrayOf(PropTypes.string),
    body : PropTypes.string,
    subject: PropTypes.string,
    date : PropTypes.string,
    id : PropTypes.number,

}

MailView.propTypes = {
    mail : PropTypes.arrayOf(mail)

}
function MailView() {
    const params = useParams()
    console.log(params)
    async function getMail(id){
        // request to get the selected mail
        const data = await axios.get("",id)
        return data
    }
    const maill = getMail(params.mailID)
    console.log(maill)


    const  [mail,setMail] = useState({
        sender : "hello",
            receiver : ["merer"],
            body : "zello World",
            subject: "fko[ss",
            date : "23/10/2024",
            id : 3,
            priority : 1,
            attachments : []
    })

    const [selectedFolder,setSelectedFolder] = useState("Inbox")


    const [folders, setFolders] = useState(["New folder", "New folder1"]);
    useEffect(() => {
        async function fetchMails() {
            const param = {
                id : params.mailID,
            };
            console.log(param);

            try {
                const data = await axios.get("http://localhost:8080/api/users/getEmail", { params: param });

                setMail(data.data);
                console.log("mail =>")
                console.log(mail)
            } catch (error) {
                console.error('Error fetching emails:', error);
            }

            const paramf = {
                email: params.user,
            };
            try {
                const response = await axios.get("http://localhost:8080/api/users/getFolders", { params: paramf });
                let x = response.data.filter((item) => ((item !== "Trash") && (item !== "Sent") && (item !== params.folderName)))
                setFolders(x);
                setSelectedFolder(x[0])
                console.log(response.data)
            } catch (error) {
                console.error('Error fetching folders:', error);
            }
        }

        fetchMails();
        console.log(folders);
    }, [params.mailID, params.user, params.folderName]);



    async function moveFolder(e) {
        try {
            const param = {
                mailId: params.mailID,
                fromFolder: params.folderName,
                toFolder: e.target.value
            };

            console.log(e.target.value);

            const url =
                `http://localhost:8080/api/users/moveFolder/${params.user}?mailId=${param.mailId}&fromFolder=${param.fromFolder}&toFolder=${e.target.id === "trash"?"Trash" :selectedFolder}`;

            await axios.post(url);
        } catch (error) {
            console.error('Error moving folder:', error);
        }
    }

    return (
        <div className="mail-view">
                <table className={"table table-striped"} >
                    <tbody>
                    <tr>
                        <td><label htmlFor="sender">From: </label></td>
                        <td>{mail.sender}
                        </td>
                    </tr>
                    <tr>
                        <td>To: </td>
                        <td>{params.folderName ==="Sent" ? mail.receivers :"You"}
                        </td>
                    </tr>
                    <tr>
                        <td>Subject: </td>
                        <td>{mail.subject}
                        </td>
                    </tr>

                    </tbody>

                </table>
            <div className={"mail-body"} style={{ whiteSpace: 'pre-wrap' }}>
                {mail.body}
            </div>

            <div style={{marginTop: "20px"}}>
                <label>Move to: </label>
                <select style={{marginRight: "10px", marginLeft: "10px"}}
                        value={selectedFolder}
                        onChange={(e) => setSelectedFolder(e.target.value)}
                >
                    {folders.map((name,index) => (<option key={index} value={name}>{name}</option>))}

                </select>
                <button className={"btn btn-outline-dark btn-sm"} onClick={moveFolder}>Move</button>
                {params.folderName !== "Trash" && <button className={"btn trash"}
                         id={"trash"}
                         style={{marginLeft: "89%", marginRight: "20px"}}
                         onClick={moveFolder}

                ><CiTrash id={"trash"} className={"trash"} style={{fontSize: "1.7rem"}}/></button>}
            </div>

            <div>
                <h6>Attachments</h6>
                {mail.attachments.map((attachment, index) => {
                    // Decode the Base64 string into a byte array
                    const byteCharacters = atob(attachment.fileContent);
                    const byteNumbers = new Array(byteCharacters.length);

                    for (let i = 0; i < byteCharacters.length; i++) {
                        byteNumbers[i] = byteCharacters.charCodeAt(i);
                    }

                    const byteArray = new Uint8Array(byteNumbers);

                    // Create a Blob from the byte array
                    const blob = new Blob([byteArray], { type: attachment.fileType });

                    return (
                        <><a
                            key={index}
                            href={URL.createObjectURL(blob)}
                            download={attachment.fileName}
                        >
                            {attachment.fileName}
                        </a><br/></>


                    );
                })}
            </div>


        </div>
    )
}

export default MailView;