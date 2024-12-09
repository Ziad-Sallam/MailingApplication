import PropTypes from 'prop-types';
import {useParams} from 'react-router-dom'


MailBox.propTypes = {
    userEmail: PropTypes.string,
}

function MailBox() {
    const params = useParams()
    console.log(params)
    return (
        <div className="d-felx flex-colum mb-3 mail-box">
            <form className={"form-control"}>
                <table className={"table table-striped"}>
                    <tbody>
                    <tr>
                        <td><label htmlFor="sender">From: </label></td>
                        <td><h6>{params.user}</h6>
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="reciever">To: </label></td>
                        <td><input className={"col-12"} type='text' name='reciever' placeholder='to'/><br/>
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="subject">Subject: </label></td>
                        <td><input className={"col-12"} type='text' name='subject' placeholder='subject'/><br/>
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="subject">Subject: </label></td>
                        <td><textarea className={"col-12"} name='message' placeholder='message'/>
                            <button className={"btn btn-primary btn-block justify-content-end"} type="submit">Submit</button>

                        </td>
                    </tr>
                    </tbody>

                </table>
            </form>
        </div>
    )
}

export default MailBox;