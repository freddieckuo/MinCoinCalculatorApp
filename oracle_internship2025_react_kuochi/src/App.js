import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import './App.css';

const REST_API_CALCULATOR_URL = "http://localhost:8080/api/submit";

export default function App() {
    const [targetAmount, setTargetAmount] = useState("");
    const [coinDenominations, setCoinDenominations] = useState(""); 
    const [coinsNeeded, setCoinsNeeded] = useState([]); 
    const [errorMessage, setErrorMessage] = useState("");

    function handleInputChange(e) {
        const { name, value } = e.target;
        if (name === "targetAmount") {
            setTargetAmount(value);
        } else if (name === "coinDenominations") {
            setCoinDenominations(value);
        }
    }

    function handleClick(e) {
		e.preventDefault();
        console.log("Confirm button is clicked");
        console.log("Target Amount:", targetAmount);
		console.log("Coin Denominations:", coinDenominations);
        setErrorMessage("");

        const requestData = {
            targetAmount: parseFloat(targetAmount), 
            coinDenominations: coinDenominations.split(/[\s,]+/).map(Number),
        }

        axios
            .post(REST_API_CALCULATOR_URL, requestData, {
                headers: {
                    "Content-Type": "application/json",
                },
                withCredentials: true,
            })
            .then((response) => {
                console.log("Response data:", response.data);
                setCoinsNeeded(response.data);
            })
            .catch((error) => {
                console.error("Error sending request data:", error);
                let message = "An unknown error occurred.";
                if (error.response) {
                    message = error.response.data;
                }
                setErrorMessage(message);
            });
        }
    
    function resetInput(e) {
        e.preventDefault(); 
        setTargetAmount("");
        setCoinDenominations("");
        setCoinsNeeded([]);
        setErrorMessage("");
        console.log("Form reset");
    }

    return (
        <div className="container">
            <div className="header">
            <p>Which Coins Do You Need?</p>
            </div>
            <form onSubmit={handleClick}>
                <label className="label" htmlFor="targetAmountInput">Target Amount: </label>
				    <input className="input"
					    type="number"
                        id="targetAmountInput"
					    name="targetAmount"
					    placeholder="Within the range between 0 to 10,000.00 up to 2 decimal places"
                        value={targetAmount || ""}
					    onChange={handleInputChange}    />
				<label className="label" htmlFor="coinDenominationsInput">Coin Denominators: </label>
				    <input className="input"
					    type="text"
                        id="coinDenominationsInput"
					    name="coinDenominations"
					    placeholder="Accepted values [0.01, 0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 50, 100, 1000]"
                        value={coinDenominations || ""}
					    onChange={handleInputChange}    />
				<button className="submit_btn" type="submit">Calculate</button>
                <br />
                <button className="reset_btn" onClick={resetInput}>Reset</button>
                <br />
                {errorMessage && <p className="error_msg">{errorMessage}</p>}
			</form>
            <br />
            <br />
            <div className="header">
            <p>Results</p>
            </div>
            <form>
            <label className="label" htmlFor="coinsNeededOutput">Coins Needed: </label>
				    <textarea className="output"
					    readOnly
                        id="coinsNeededOutput"
					    name="coinsNeeded"
					    value={coinsNeeded.join(", ")}    />
            </form>
        </div>
);
}