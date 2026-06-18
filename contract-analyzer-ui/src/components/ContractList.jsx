import React from "react";
import contracts from "../mocks/contracts.json";

export default function ContractList() {
  return (
    <ul>
      {contracts.map((c) => (
        <li key={c.id}>{c.title}</li>
      ))}
    </ul>
  );
}
