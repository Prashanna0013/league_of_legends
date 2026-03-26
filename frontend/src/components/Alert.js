import React from 'react';
import './Alert.css';

function Alert({ type, message, onClose }) {
  React.useEffect(() => {
    const timer = setTimeout(onClose, 4000);
    return () => clearTimeout(timer);
  }, [onClose]);

  const icon = type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle';

  return (
    <div className={`alert alert-${type}`}>
      <i className={`fas ${icon}`}></i>
      <span>{message}</span>
      <button className="alert-close" onClick={onClose}>
        <i className="fas fa-times"></i>
      </button>
    </div>
  );
}

export default Alert;

