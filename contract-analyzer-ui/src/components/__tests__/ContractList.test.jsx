import React from 'react';
import { render, screen } from '@testing-library/react';
import ContractList from '../ContractList';

test('renders contract titles', () => {
  render(<ContractList />);
  expect(screen.getByText(/NDA Agreement/i)).toBeInTheDocument();
  expect(screen.getByText(/Service Level Agreement/i)).toBeInTheDocument();
});
