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
    mail : PropTypes.oneOfType(mail)

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
            priority : 1
    })



    useEffect(() => {
        async function fetchMails() {
            const param = {
                id : params.mailID,
            };
            console.log(param);

            try {
                const data = await axios.get("http://localhost:8080/api/users/getEmail", { params: param });
                console.log(data.data.body);
                setMail(data.data);
            } catch (error) {
                console.error('Error fetching emails:', error);
            }
        }

        fetchMails();
    }, [params.mailID]);
    return (
        <div className="mail-view">
                <table className={"table table-striped"}>
                    <tbody>
                    <tr>
                        <td><label htmlFor="sender">From: </label></td>
                        <td>{mail.sender}
                        </td>
                    </tr>
                    <tr>
                        <td>To: </td>
                        <td>You
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
                <select style={{marginRight: "10px", marginLeft: "10px"}}>
                    <option value={"folder1"}>folder1</option>
                    <option value={"folder2"}>folder2</option>
                    <option value={"folder3"}>folder3</option>
                </select>
                <button className={"btn btn-outline-dark btn-sm"}>Move</button>
                <button className={"btn "}
                        style={{marginLeft: "89%", marginRight: "20px"}}
                ><CiTrash style={{fontSize: "1.7rem"}}/></button>
            </div>


        </div>
    )
}

export default MailView;